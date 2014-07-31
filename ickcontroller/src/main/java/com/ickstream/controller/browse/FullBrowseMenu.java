/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.controller.browse;

import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.service.content.ContentResponse;

import java.util.*;

public class FullBrowseMenu extends AbstractBrowseMenu {
    private Map<String, TypeMenuItem> typeMenus = new HashMap<String, TypeMenuItem>();
    private Map<String, Integer> typeMenuPriorities = new HashMap<String, Integer>();

    public FullBrowseMenu(ServiceController service, List<TypeMenuItem> typeMenusTemplates) {
        super(service);
        for (TypeMenuItem typeMenu : typeMenusTemplates) {
            this.typeMenus.put(typeMenu.getId(), typeMenu);
        }
        initTypeMenuPriorities(typeMenusTemplates);
    }

    private void initTypeMenuPriorities(List<TypeMenuItem> typeMenuTemplates) {
        int i = typeMenus.size();
        for (TypeMenuItem typeMenu : typeMenuTemplates) {
            this.typeMenuPriorities.put(typeMenu.getId(), i);
            i--;
        }
    }

    public void findContexts(final String language, final ResponseListener<BrowseResponse> listener) {
        getProtocol(language, new ResponseListener<Boolean>() {
            @Override
            public void onResponse(Boolean contentResponse) {
                listener.onResponse(createContextsResponse());
            }
        });
    }

    public void findItemsInContext(final String contextId, final String language, final MenuItem contentItem, final ResponseListener<BrowseResponse> listener) {
        if (contentItem == null || contentItem instanceof ContextMenuItem) {
            getProtocol(language, new ResponseListener<Boolean>() {
                @Override
                public void onResponse(Boolean contentResponse) {
                    BrowseResponse response = createTypeMenuResponse(contextId, contentItem);
                    listener.onResponse(response);
                }
            });
        } else {
            findItemsInContextByType(contextId, language, null, contentItem, listener);
        }
    }

    public void findItemsInContextByType(final String contextId, final String language, final String type, final MenuItem contentItem, final ResponseListener<BrowseResponse> listener) {
        final Set<String> usedTypes = new HashSet<String>();
        MenuItem parent = contentItem;
        while (parent != null) {
            if (parent instanceof ContentMenuItem && parent.getType() != null && !parent.getType().equals("menu") && !parent.getType().equals("category")) {
                usedTypes.add(parent.getType());
            }
            parent = parent.getParent();
        }

        getProtocol(language, new ResponseListener<Boolean>() {
            @Override
            public void onResponse(Boolean contentResponse) {
                Map<String, String> parameters = createChildRequestParameters(contextId, type, contentItem, usedTypes);
                if (parameters != null) {
                    Set<String> excludedTypes = new HashSet<String>();
                    final Set<String> availableChildItems;
                    if (parameters.containsKey("type")) {
                        excludedTypes.add(parameters.get("type"));
                        if (!parameters.get("type").equals("category") && contentItem instanceof ContentMenuItem && contentItem.getType().equals("category")) {
                            excludedTypes.add("category");
                        }
                        if (!parameters.get("type").equals("menu") && contentItem instanceof ContentMenuItem && contentItem.getType().equals("menu")) {
                            excludedTypes.add("menu");
                        }
                        availableChildItems = findPossibleItemTypes(contextId, type, contentItem, usedTypes, excludedTypes);
                    } else {
                        availableChildItems = new HashSet<String>();
                    }

                    final String context = parameters.remove("contextId");
                    service.findItems(null, context, language, new HashMap<String, Object>(parameters), new MessageHandlerAdapter<ContentResponse>() {
                        @Override
                        public void onMessage(ContentResponse message) {
                            if (message != null) {
                                BrowseResponse browseResponse = new BrowseResponse();
                                browseResponse.setLastChanged(message.getLastChanged());
                                browseResponse.setCount(message.getCount());
                                browseResponse.setCountAll(message.getCountAll());
                                browseResponse.setOffset(message.getOffset());
                                browseResponse.setItems(new ArrayList<MenuItem>(message.getCount()));
                                if (message.getItems().size() > 1) {
                                    for (String availableChildItem : availableChildItems) {
                                        browseResponse.getItems().add(new TypeMenuItem(service, context, availableChildItem, "All " + availableChildItem + "s", null, contentItem));
                                    }
                                }
                                for (ContentItem item : message.getItems()) {
                                    browseResponse.getItems().add(new ContentMenuItem(service, context, item, contentItem));
                                }
                                listener.onResponse(browseResponse);
                            }
                        }
                    }, 10000);
                } else {
                    listener.onResponse(null);
                }
            }
        });
    }

    private BrowseResponse createContextsResponse() {
        BrowseResponse response = new BrowseResponse();
        response.setItems(new ArrayList<MenuItem>());
        response.setItems(new ArrayList<MenuItem>(findPossibleTopLevelContexts()));

        if (response.getItems().size() > 1) {
            Iterator<MenuItem> it = response.getItems().iterator();
            while (it.hasNext()) {
                if (it.next().getContextId().equals("allMusic")) {
                    it.remove();
                }
            }
        }

        response.setOffset(0);
        response.setCount(response.getItems().size());
        response.setCountAll(response.getItems().size());
        return response;
    }

    private BrowseResponse createTypeMenuResponse(String contextId, MenuItem parent) {
        BrowseResponse response = new BrowseResponse();
        response.setItems(new ArrayList<MenuItem>());
        List<String> possibleTypeRequests = findPossibleTypeRequests(contextId, parent);
        for (String possibleTypeRequest : possibleTypeRequests) {
            if (typeMenus.containsKey(possibleTypeRequest)) {
                TypeMenuItem template = typeMenus.get(possibleTypeRequest);
                response.getItems().add(new TypeMenuItem(service, contextId, possibleTypeRequest, template.getText(), template.getImage(), parent));
            } else {
                response.getItems().add(new TypeMenuItem(service, contextId, possibleTypeRequest, possibleTypeRequest, null, parent));
            }
        }
        response.setOffset(0);
        response.setCount(response.getItems().size());
        response.setCountAll(response.getItems().size());
        return response;
    }

    private Map<String, String> createChildRequestParameters(String contextId, String type, MenuItem parentItem, Set<String> excludedTypes) {
        if (contextId != null) {
            // Find within the same context
            Map<String, String> result = createChildRequestParametersFromContext(contextId, type, parentItem, excludedTypes);
            if (result != null) {
                return result;
            }
        }
        if (contextId == null || !contextId.equals("allMusic")) {
            // Find within the allMusic context
            Map<String, String> result = createChildRequestParametersFromContext("allMusic", type, parentItem, excludedTypes);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private Set<String> findPossibleItemTypes(String contextId, String type, MenuItem parentItem, Set<String> includedTypes, Set<String> excludedTypes) {
        if (contextId != null) {
            // Find within the same context
            Set<String> result = findPossibleItemTypesFromContext(contextId, type, parentItem, includedTypes, excludedTypes);
            if (result != null) {
                return result;
            }
        }
        if (contextId == null || !contextId.equals("allMusic")) {
            // Find within the allMusic context
            Set<String> result = findPossibleItemTypesFromContext("allMusic", type, parentItem, includedTypes, excludedTypes);
            if (result != null) {
                return result;
            }
        }
        return new HashSet<String>();
    }

    private Map<String, String> createChildRequestParametersFromContext(String contextId, String type, MenuItem parentItem, Set<String> excludedTypes) {
        return createChildRequestParametersFromContext(contextId, type, parentItem, excludedTypes, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> entry1, Map<String, String> entry2) {
                if (entry1.containsKey("type") && !entry2.containsKey("type")) {
                    return -1;
                } else if (!entry1.containsKey("type") && entry2.containsKey("type")) {
                    return 1;
                } else {
                    return getTypePriority(entry1.get("type")) - getTypePriority(entry2.get("type"));
                }
            }
        });
    }

    private Integer getTypePriority(String type) {
        if (typeMenuPriorities.containsKey(type)) {
            return typeMenuPriorities.get(type);
        } else {
            return 0;
        }
    }
}
