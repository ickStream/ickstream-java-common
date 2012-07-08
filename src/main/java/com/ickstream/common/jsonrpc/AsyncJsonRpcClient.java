/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsyncJsonRpcClient implements JsonRpcRequestHandler, JsonRpcResponseHandler {
    private int id = 1;
    private MessageSender messageSender;
    private ObjectMapper mapper;
    private final Map<String, MessageHandlerEntry> messageHandlers = new HashMap<String, MessageHandlerEntry>();
    private final Map<String, List<MessageHandlerEntry>> notificationHandlers = new HashMap<String, List<MessageHandlerEntry>>();

    private static class MessageHandlerEntry {
        private Class type;
        private MessageHandler handler;

        private MessageHandlerEntry(Class type, MessageHandler handler) {
            this.type = type;
            this.handler = handler;
        }
    }

    public AsyncJsonRpcClient(MessageSender messageSender) {
        this.messageSender = messageSender;
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    protected MessageSender getMessageSender() {
        return messageSender;
    }

    protected String sendRequest(String method, Object params) {
        return sendRequest(method, params, null, null);
    }

    protected String sendRequest(String method, Object params, Class messageResponseClass, MessageHandler messageHandler) {
        Integer id;
        synchronized (messageHandlers) {
            id = this.id;
            this.id++;
            if (messageResponseClass != null && messageHandler != null) {
                messageHandlers.put("" + id, new MessageHandlerEntry(messageResponseClass, messageHandler));
            }
        }
        JsonRpcRequest jsonRpcRequest = new JsonRpcRequest();
        jsonRpcRequest.setId("" + id);
        jsonRpcRequest.setMethod(method);
        if (params != null) {
            jsonRpcRequest.setParams(mapper.valueToTree(params));
        }
        try {
            String requestString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.valueToTree(jsonRpcRequest));
            messageSender.sendMessage(requestString);
            return jsonRpcRequest.getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void addNotificationListener(String method, Class<T> messageParameterType, MessageHandler<T> messageHandler) {
        synchronized (notificationHandlers) {
            if (!notificationHandlers.containsKey(method)) {
                notificationHandlers.put(method, new ArrayList<MessageHandlerEntry>());
            }
            notificationHandlers.get(method).add(new MessageHandlerEntry(messageParameterType, messageHandler));
        }
    }

    public <T> void removeNotificationListener(String method, MessageHandler<T> messageHandler) {
        synchronized (notificationHandlers) {
            List<MessageHandlerEntry> notificationHandlers = this.notificationHandlers.get(method);
            if (notificationHandlers != null) {
                MessageHandlerEntry toRemove = null;
                for (MessageHandlerEntry notificationHandler : notificationHandlers) {
                    if (notificationHandler.handler == messageHandler) {
                        toRemove = notificationHandler;
                        break;
                    }
                }
                if (toRemove != null) {
                    notificationHandlers.remove(toRemove);
                }
            }
        }
    }

    @Override
    public void onRequest(JsonRpcRequest message) {
        List<MessageHandlerEntry> notificationHandlers;
        synchronized (this.notificationHandlers) {
            notificationHandlers = this.notificationHandlers.get(message.getMethod());
        }

        if (notificationHandlers != null) {
            Object params = null;
            for (MessageHandlerEntry notificationHandler : notificationHandlers) {
                try {
                    if (params == null || !params.getClass().equals(notificationHandler.getClass())) {
                        if (notificationHandler.type.isInstance(message)) {
                            params = message;
                        } else {
                            params = mapper.treeToValue(message.getParams(), notificationHandler.type);
                        }
                    }
                    notificationHandler.handler.onMessage(params);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResponse(JsonRpcResponse message) {
        MessageHandlerEntry messageHandler;
        synchronized (messageHandlers) {
            messageHandler = messageHandlers.remove(message.getId());
        }
        if (messageHandler != null) {
            try {
                Object params = null;
                if (messageHandler.type != null) {
                    if (messageHandler.type.isInstance(message)) {
                        params = message;
                    } else if (message.getResult() != null) {
                        params = mapper.treeToValue(message.getResult(), messageHandler.type);
                    }
                }
                messageHandler.handler.onMessage(params);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
