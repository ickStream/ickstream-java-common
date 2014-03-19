/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.service.content.GetProtocolDescriptionResponse;
import com.ickstream.protocol.service.content.ProtocolDescriptionContext;
import com.ickstream.protocol.service.content.RequestDescription;

import java.util.*;

public abstract class AbstractBrowseMenu implements BrowseMenu {
    protected ServiceController service;
    private final Map<String, ProtocolDescriptionContext> supportedRequests = new HashMap<String, ProtocolDescriptionContext>();

    public AbstractBrowseMenu(ServiceController service) {
        this.service = service;
    }

    protected void getProtocol(final ResponseListener<Boolean> listener) {
        if (supportedRequests.size() == 0) {
            service.getProtocolDescription(null, new MessageHandlerAdapter<GetProtocolDescriptionResponse>() {
                @Override
                public void onMessage(GetProtocolDescriptionResponse message) {
                    synchronized (supportedRequests) {
                        supportedRequests.clear();
                        for (ProtocolDescriptionContext context : message.getItems()) {
                            supportedRequests.put(context.getContextId(), context);

                        }
                    }
                    listener.onResponse(Boolean.TRUE);
                }
            }, 10000);
        } else {
            listener.onResponse(Boolean.FALSE);
        }
    }

    protected Map<String, String> createChildRequestParametersFromContext(String contextId, String type, MenuItem parentItem, Set<String> excludedTypes, Comparator<Map<String, String>> comparator) {
        ProtocolDescriptionContext context;
        synchronized (supportedRequests) {
            context = supportedRequests.get(contextId);
        }
        if (context == null) {
            return null;
        }
        List<Map<String, String>> possibleRequests = findPossibleRequests(context, type, parentItem);
        boolean supported = false;
        Map<String, String> supportedParameters = new HashMap<String, String>();
        Map<String, String> preferredSupportedParameters = new HashMap<String, String>();

        for (Map<String, String> possibleRequest : possibleRequests) {
            if (possibleRequest.containsKey("type") && excludedTypes != null && excludedTypes.contains(possibleRequest.get("type"))) {
                // We aren't interested in excluded types
            } else if (parentItem instanceof ContentMenuItem && parentItem.getType() != null && (!possibleRequest.containsKey(parentItem.getType() + "Id") || !possibleRequest.get(parentItem.getType() + "Id").equals(parentItem.getId()))) {
                // We aren't interested in requests unless they filter by parent item
            } else if (supportedParameters.size() == possibleRequest.size() + 1 &&
                    supportedParameters.containsKey("type") &&
                    !possibleRequest.containsKey("type")) {

                // If we can request without type we should do that
                supportedParameters = possibleRequest;
            } else if (supportedParameters.size() + 1 == possibleRequest.size() &&
                    !supportedParameters.containsKey("type") &&
                    possibleRequest.containsKey("type")) {
                // Keep the old one, if we can request without type we should do that
            } else if (supportedParameters.size() < possibleRequest.size()) {
                // We should always prefer choices with more criteria
                supportedParameters = possibleRequest;
                if (parentItem != null && parentItem.getPreferredChildItems() != null && parentItem.getPreferredChildItems().size() > 0 && supportedParameters.get("type").equals(parentItem.getPreferredChildItems().get(0))) {
                    preferredSupportedParameters = supportedParameters;
                }
            } else if (supportedParameters.size() == possibleRequest.size() &&
                    comparator.compare(supportedParameters, possibleRequest) < 0) {
                // With equal priority we should prefer items which show more items
                supportedParameters = possibleRequest;
                if (parentItem != null && parentItem.getPreferredChildItems() != null && parentItem.getPreferredChildItems().size() > 0 && supportedParameters.get("type").equals(parentItem.getPreferredChildItems().get(0))) {
                    preferredSupportedParameters = supportedParameters;
                }
            } else if (supportedParameters.size() == possibleRequest.size() &&
                    parentItem != null &&
                    !supportedParameters.containsKey(parentItem.getType() + "Id") &&
                    possibleRequest.containsKey(parentItem.getType() + "Id")) {
                // With equal number of priority we should prefer items which filter by nearest parent
                supportedParameters = possibleRequest;
                if (parentItem.getPreferredChildItems() != null && parentItem.getPreferredChildItems().size() > 0 && supportedParameters.get("type").equals(parentItem.getPreferredChildItems().get(0))) {
                    preferredSupportedParameters = supportedParameters;
                }
            } else if (preferredSupportedParameters.size() == 0 && parentItem != null && parentItem.getPreferredChildItems() != null && parentItem.getPreferredChildItems().size() > 0 && possibleRequest.containsKey("type") && possibleRequest.get("type").equals(parentItem.getPreferredChildItems().get(0))) {
                // If type is preferred, we should use it
                preferredSupportedParameters = possibleRequest;
            }
        }
        if (preferredSupportedParameters.size() > 0) {
            supportedParameters = preferredSupportedParameters;
        }
        if (supportedParameters.size() > 0) {
            for (String supportedParameter : supportedParameters.keySet()) {
                if ((!supportedParameter.equals("contextId") && !supportedParameter.equals("type")) || !(parentItem instanceof ContentMenuItem)) {
                    supported = true;
                }
            }
        }

        if (supported) {
            return supportedParameters;
        }
        return null;
    }

    protected Set<String> findPossibleItemTypesFromContext(String contextId, String type, MenuItem parentItem, Set<String> includedTypes, Set<String> excludedTypes) {
        ProtocolDescriptionContext context;
        synchronized (supportedRequests) {
            context = supportedRequests.get(contextId);
        }
        if (context == null) {
            return null;
        }
        List<Map<String, String>> possibleRequests = findPossibleRequests(context, type, parentItem);
        boolean supported = false;
        Set<String> supportedRequests = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });

        for (Map<String, String> possibleRequest : possibleRequests) {
            if (possibleRequest.containsKey("type") && possibleRequest.containsKey("contextId") && excludedTypes != null && !excludedTypes.contains(possibleRequest.get("type"))) {

                boolean include = true;
                for (String includedType : includedTypes) {
                    if (!possibleRequest.containsKey(includedType + "Id")) {
                        include = false;
                        break;
                    }
                }
                if (include && parentItem instanceof ContentMenuItem && parentItem.getType() != null && (!possibleRequest.containsKey(parentItem.getType() + "Id") || !possibleRequest.get(parentItem.getType() + "Id").equals(parentItem.getId()))) {
                    include = false;
                }
                if (include) {
                    supportedRequests.add(possibleRequest.get("type"));
                }
            }
        }

        if (supportedRequests.size() > 0) {
            return supportedRequests;
        }
        return null;
    }

    protected List<ContextMenuItem> findPossibleTopLevelContexts() {
        List<ContextMenuItem> response = new ArrayList<ContextMenuItem>();
        synchronized (supportedRequests) {
            for (ProtocolDescriptionContext context : supportedRequests.values()) {
                boolean supported = false;
                for (RequestDescription supportedRequest : context.getSupportedRequests()) {
                    for (List<String> parameters : supportedRequest.getParameters()) {
                        boolean unsupported = false;
                        for (String parameter : parameters) {
                            if (!parameter.equals("contextId") && !parameter.equals("type")) {
                                unsupported = true;
                                break;
                            }
                        }
                        if (!unsupported) {
                            supported = true;
                            break;
                        }
                    }
                    if (supported) {
                        break;
                    }
                }
                if (supported) {
                    response.add(new ContextMenuItem(service, context.getContextId(), context.getName(), null));
                }
            }
        }
        return response;
    }

    protected List<String> findPossibleTypeRequests(String contextId, MenuItem parentItem) {
        ProtocolDescriptionContext context;
        synchronized (supportedRequests) {
            context = supportedRequests.get(contextId);
        }
        if (context == null) {
            return new ArrayList<String>();
        }
        List<String> availableTypeRequests = new ArrayList<String>();
        for (RequestDescription supportedRequest : context.getSupportedRequests()) {
            boolean supported = false;
            for (List<String> parameters : supportedRequest.getParameters()) {
                boolean unsupported = false;
                for (String parameter : parameters) {
                    if (!parameter.equals("contextId") && !parameter.equals("type")) {
                        unsupported = true;
                        break;
                    }
                }
                if (!parameters.contains("type")) {
                    break;
                }
                if (!unsupported) {
                    supported = true;
                    break;
                }
            }
            if (supported) {
                availableTypeRequests.add(supportedRequest.getType());
            }
        }
        return availableTypeRequests;
    }

    protected List<Map<String, String>> findPossibleRequests(ProtocolDescriptionContext context, String type, MenuItem parentItem) {
        boolean supported = false;
        String supportedType = null;
        List<Map<String, String>> possibleRequests = new ArrayList<Map<String, String>>();
        for (RequestDescription supportedRequest : context.getSupportedRequests()) {
            if (type == null || (supportedRequest.getType() != null && supportedRequest.getType().equals(type))) {
                for (List<String> parameters : supportedRequest.getParameters()) {
                    boolean unsupported = false;
                    for (String parameter : parameters) {
                        if (!parameter.equals("contextId") && !parameter.equals("type") && (parentItem == null || (parentItem instanceof ContentMenuItem && getParameterFromItem(parameter, parentItem) == null))) {
                            unsupported = true;
                            break;
                        }
                    }
                    if (type != null && !parameters.contains("type")) {
                        unsupported = true;
                    }
                    if (!unsupported) {
                        Map<String, String> possibleParameters = new HashMap<String, String>();
                        for (String parameter : parameters) {
                            if (parameter.equals("type")) {
                                possibleParameters.put("type", type != null ? type : supportedRequest.getType());
                            } else if (parameter.equals("contextId")) {
                                possibleParameters.put("contextId", context.getContextId());
                            } else {
                                String value = getParameterFromItem(parameter, parentItem);
                                if (value != null) {
                                    possibleParameters.put(parameter, value);
                                }
                            }
                        }
                        possibleRequests.add(possibleParameters);
                    }
                }
            }
        }
        return possibleRequests;
    }

    protected String getParameterFromItem(String parameter, MenuItem item) {
        if (item instanceof ContentMenuItem) {
            if (parameter.equals(item.getType() + "Id")) {
                return item.getId();
            } else if (item.getParent() != null) {
                return getParameterFromItem(parameter, item.getParent());
            }
        } else if (item.getParent() != null) {
            return getParameterFromItem(parameter, item.getParent());
        }
        return null;
    }
}
