/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.service.content.ProtocolDescriptionResponse;
import com.ickstream.protocol.service.content.ProtocolDescriptionResponseContext;
import com.ickstream.protocol.service.content.RequestDescription;

import java.util.*;

public abstract class AbstractBrowseMenu implements BrowseMenu {
    protected ServiceController service;
    private final Map<String, ProtocolDescriptionResponseContext> supportedRequests = new HashMap<String, ProtocolDescriptionResponseContext>();

    public AbstractBrowseMenu(ServiceController service) {
        this.service = service;
    }

    protected void getProtocol(final ResponseListener<Boolean> listener) {
        if (supportedRequests.size() == 0) {
            service.getProtocolDescription(null, new MessageHandlerAdapter<ProtocolDescriptionResponse>() {
                @Override
                public void onMessage(ProtocolDescriptionResponse message) {
                    synchronized (supportedRequests) {
                        supportedRequests.clear();
                        for (ProtocolDescriptionResponseContext context : message.getItems()) {
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

    protected Map<String, String> createChildRequestParametersFromContext(String contextId, String type, MenuItem parentItem, Comparator<Map<String, String>> comparator) {
        ProtocolDescriptionResponseContext context;
        synchronized (supportedRequests) {
            context = supportedRequests.get(contextId);
        }
        if (context == null) {
            return null;
        }
        List<Map<String, String>> possibleRequests = findPossibleRequests(context, type, parentItem);
        boolean supported = false;
        Map<String, String> supportedParameters = new HashMap<String, String>();

        for (Map<String, String> possibleRequest : possibleRequests) {
            if (supportedParameters.size() == possibleRequest.size() + 1 &&
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
            } else if (supportedParameters.size() == possibleRequest.size() &&
                    comparator.compare(supportedParameters, possibleRequest) < 0) {
                // With equal priority we should prefer items which show more items
                supportedParameters = possibleRequest;
            } else if (supportedParameters.size() == possibleRequest.size() &&
                    parentItem != null &&
                    !supportedParameters.containsKey(parentItem.getType() + "Id") &&
                    possibleRequest.containsKey(parentItem.getType() + "Id")) {
                // With equal number of priority we should prefer items which filter by nearest parent
                supportedParameters = possibleRequest;
            }
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

    protected List<ContextMenuItem> findPossibleTopLevelContexts() {
        List<ContextMenuItem> response = new ArrayList<ContextMenuItem>();
        synchronized (supportedRequests) {
            for (ProtocolDescriptionResponseContext context : supportedRequests.values()) {
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
        ProtocolDescriptionResponseContext context;
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

    protected List<Map<String, String>> findPossibleRequests(ProtocolDescriptionResponseContext context, String type, MenuItem parentItem) {
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
