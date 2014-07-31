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

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.service.content.*;

import java.util.*;

public class PreferredBrowseMenu implements BrowseMenu {
    protected ServiceController service;
    protected final Map<String, Map<String, Map<String, RequestDescription2>>> contexts = new HashMap<String, Map<String, Map<String, RequestDescription2>>>();
    protected final List<PreferredMenuItem> preferredMenuItems = new ArrayList<PreferredMenuItem>();

    public PreferredBrowseMenu(ServiceController service) {
        this.service = service;
    }

    @Override
    public void findChilds(final String language, final MenuItem contentItem, final ResponseListener<BrowseResponse> listener) {
        getProtocolDescription2(language, new BrowseMenu.ResponseListener<Boolean>() {
            @Override
            public void onResponse(Boolean contentResponse) {
                getPreferredMenus(language, new BrowseMenu.ResponseListener<Boolean>() {
                    @Override
                    public void onResponse(Boolean contentResponse) {
                        if (contentItem != null) {
                            if (((AbstractPreferredMenuItem) contentItem).getPreferredMenuItem().getChildItems() != null) {
                                List<PreferredMenuItem> preferredMenuItems = ((AbstractPreferredMenuItem) contentItem).getPreferredMenuItem().getChildItems();
                                BrowseResponse response = new BrowseResponse();
                                response.setItems(new ArrayList<MenuItem>());
                                for (PreferredMenuItem preferredMenuItem : preferredMenuItems) {
                                    if (!preferredMenuItem.getType().equals("search")) {
                                        response.getItems().add(new PreferredTextMenuItem(preferredMenuItem, service, preferredMenuItem.getText(), contentItem));
                                    }
                                }
                                listener.onResponse(response);
                            } else if (((AbstractPreferredMenuItem) contentItem).getPreferredMenuItem().getChildRequest() != null) {
                                final PreferredMenuRequest childRequest = ((AbstractPreferredMenuItem) contentItem).getPreferredMenuItem().getChildRequest();
                                String requestId = childRequest.getRequest();
                                Map<String, Object> request = getRequest(requestId, contentItem);
                                service.findItems(null, (String) request.get("contextId"), language, request, new MessageHandlerAdapter<ContentResponse>() {
                                    @Override
                                    public void onMessage(ContentResponse message) {
                                        BrowseResponse response = new BrowseResponse();
                                        response.setItems(new ArrayList<MenuItem>());
                                        response.setLastChanged(message.getLastChanged());

                                        for (ContentItem item : message.getItems()) {
                                            response.getItems().add(new PreferredContentMenuItem(childRequest, service, null, item, contentItem));
                                        }
                                        listener.onResponse(response);
                                    }
                                });
                            } else if ((contentItem instanceof PreferredContentMenuItem && ((PreferredContentMenuItem) contentItem).getContentItem().getPreferredChildRequest() != null)) {

                                final PreferredMenuRequest childRequest = new PreferredMenuRequest(((PreferredContentMenuItem) contentItem).getContentItem().getPreferredChildRequest());
                                String requestId = childRequest.getRequest();
                                Map<String, Object> request = getRequest(requestId, contentItem);
                                service.findItems(null, (String) request.get("contextId"), language, request, new MessageHandlerAdapter<ContentResponse>() {
                                    @Override
                                    public void onMessage(ContentResponse message) {
                                        BrowseResponse response = new BrowseResponse();
                                        response.setItems(new ArrayList<MenuItem>());
                                        response.setLastChanged(message.getLastChanged());

                                        for (ContentItem item : message.getItems()) {
                                            response.getItems().add(new PreferredContentMenuItem(childRequest, service, null, item, contentItem));
                                        }
                                        listener.onResponse(response);
                                    }
                                });
                            } else {
                                listener.onResponse(new BrowseResponse());
                            }
                        } else {
                            BrowseResponse response = new BrowseResponse();
                            response.setItems(new ArrayList<MenuItem>());
                            for (PreferredMenuItem preferredMenuItem : preferredMenuItems) {
                                if (!preferredMenuItem.getType().equals("search")) {
                                    response.getItems().add(new PreferredTextMenuItem(preferredMenuItem, service, preferredMenuItem.getText(), contentItem));
                                }
                            }
                            listener.onResponse(response);
                        }
                    }
                });
            }
        });
    }

    private Map<String, Object> getRequest(String request, MenuItem contentItem) {
        for (Map.Entry<String, Map<String, Map<String, RequestDescription2>>> contextEntry : contexts.entrySet()) {
            for (Map.Entry<String, Map<String, RequestDescription2>> typeEntry : contextEntry.getValue().entrySet()) {
                if (typeEntry.getValue().containsKey(request)) {
                    RequestDescription2 spec = typeEntry.getValue().get(request);
                    Map<String, Object> params = new HashMap<String, Object>();
                    if (spec.getParameters().contains("contextId")) {
                        params.put("contextId", contextEntry.getKey());
                    }
                    if (spec.getParameters().contains("type")) {
                        params.put("type", typeEntry.getKey());
                    }
                    if (spec.getValues() != null) {
                        Iterator<Map.Entry<String, JsonNode>> it = spec.getValues().fields();
                        while (it.hasNext()) {
                            Map.Entry<String, JsonNode> param = it.next();
                            if (param.getValue().isNumber()) {
                                params.put(param.getKey(), param.getValue().numberValue());
                            } else if (param.getValue().isTextual()) {
                                params.put(param.getKey(), param.getValue().textValue());
                            }
                        }
                    }
                    if (spec.getParameters() != null && spec.getParameters().size() > 0) {
                        MenuItem item = contentItem;
                        while (item != null) {
                            if (item.getType() != null && spec.getParameters().contains(item.getType() + "Id") && !params.containsKey(item.getType() + "Id")) {
                                params.put(item.getType() + "Id", item.getId());
                            }
                            item = item.getParent();
                        }
                    }
                    return params;
                }
            }
        }
        return null;
    }

    protected void getProtocolDescription2(String language, final ResponseListener<Boolean> listener) {
        if (contexts.size() == 0) {
            service.getProtocolDescription2(new GetProtocolDescriptionRequest(language), new MessageHandlerAdapter<GetProtocolDescription2Response>() {
                @Override
                public void onMessage(GetProtocolDescription2Response message) {
                    synchronized (contexts) {
                        contexts.clear();
                        for (ProtocolDescription2Context context : message.getItems()) {
                            contexts.put(context.getContextId(), context.getSupportedRequests());
                        }
                    }
                    listener.onResponse(Boolean.TRUE);
                }
            }, 10000);
        } else {
            listener.onResponse(Boolean.FALSE);
        }
    }

    protected void getPreferredMenus(String language, final BrowseMenu.ResponseListener<Boolean> listener) {
        if (preferredMenuItems.size() == 0) {
            service.getPreferredMenus(new GetPreferredMenusRequest(language), new MessageHandlerAdapter<GetPreferredMenusResponse>() {
                @Override
                public void onMessage(GetPreferredMenusResponse message) {
                    synchronized (preferredMenuItems) {
                        preferredMenuItems.clear();
                        preferredMenuItems.addAll(message.getItems());
                    }
                    listener.onResponse(Boolean.TRUE);
                }
            }, 10000);
        } else {
            listener.onResponse(Boolean.FALSE);
        }
    }
}
