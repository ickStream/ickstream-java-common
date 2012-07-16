/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import java.util.*;

public class AsyncJsonRpcClient implements JsonRpcRequestHandler, JsonRpcResponseHandler {
    private MessageSender messageSender;
    private JsonHelper jsonHelper = new JsonHelper();
    private final Map<Object, MessageHandlerEntry> messageHandlers = new HashMap<Object, MessageHandlerEntry>();
    private final Map<String, List<MessageHandlerEntry>> notificationHandlers = new HashMap<String, List<MessageHandlerEntry>>();
    private Integer defaultTimeout;
    private IdProvider idProvider;

    private static class MessageHandlerEntry {
        private Class type;
        private MessageHandler handler;
        private Timer timer;

        private MessageHandlerEntry(Class type, MessageHandler handler) {
            this.type = type;
            this.handler = handler;
        }

        private MessageHandlerEntry(Class type, MessageHandler handler, Timer timer) {
            this.type = type;
            this.handler = handler;
            this.timer = timer;
        }
    }

    public AsyncJsonRpcClient(MessageSender messageSender) {
        this(messageSender, null, null);
    }

    public AsyncJsonRpcClient(MessageSender messageSender, IdProvider idProvider) {
        this(messageSender, idProvider, null);
    }

    public AsyncJsonRpcClient(MessageSender messageSender, Integer defaultTimeout) {
        this(messageSender, null, defaultTimeout);
    }

    public AsyncJsonRpcClient(MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
        this.messageSender = messageSender;
        this.defaultTimeout = defaultTimeout;
        if (idProvider == null) {
            this.idProvider = new GlobalIdProvider();
        } else {
            this.idProvider = idProvider;
        }
    }

    protected MessageSender getMessageSender() {
        return messageSender;
    }

    protected String sendRequest(String method, Object params) {
        return sendRequest(method, params, null, null);
    }

    protected String sendRequest(String method, Object params, Class messageResponseClass, MessageHandler messageHandler) {
        return sendRequest(method, params, messageResponseClass, messageHandler, null);
    }

    protected String sendRequest(String method, Object params, Class messageResponseClass, MessageHandler messageHandler, Integer timeout) {
        Object id;
        synchronized (messageHandlers) {
            id = idProvider.getNextId().toString();
            if (messageResponseClass != null && messageHandler != null) {
                if ((timeout == null && defaultTimeout == null) || (timeout != null && timeout < 0)) {
                    messageHandlers.put("" + id, new MessageHandlerEntry(messageResponseClass, messageHandler));
                } else {
                    final String currentId = "" + id;
                    messageHandlers.put(currentId, new MessageHandlerEntry(messageResponseClass, messageHandler, new Timer()));
                    messageHandlers.get(currentId).timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            MessageHandlerEntry entry;
                            synchronized (messageHandlers) {
                                entry = messageHandlers.remove(currentId);
                            }
                            if (entry != null) {
                                entry.handler.onTimeout();
                                entry.handler.onFinished();
                            }
                        }
                    }, timeout != null ? timeout : defaultTimeout);
                }
            }
        }
        JsonRpcRequest jsonRpcRequest = new JsonRpcRequest();
        jsonRpcRequest.setId("" + id);
        jsonRpcRequest.setMethod(method);
        if (params != null) {
            jsonRpcRequest.setParams(jsonHelper.objectToJson(params));
        }
        String requestString = jsonHelper.objectToString(jsonRpcRequest);
        if (requestString != null) {
            messageSender.sendMessage(requestString);
            return jsonRpcRequest.getId();
        } else {
            throw new RuntimeException("Unable to convert message to JSON");
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

    protected void removeMessageHandler(String id) {
        synchronized (messageHandlers) {
            messageHandlers.remove(id);
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
    public boolean onRequest(JsonRpcRequest message) {
        List<MessageHandlerEntry> notificationHandlers;
        synchronized (this.notificationHandlers) {
            notificationHandlers = this.notificationHandlers.get(message.getMethod());
        }

        if (notificationHandlers != null) {
            Object params = null;
            for (MessageHandlerEntry notificationHandler : notificationHandlers) {
                if (params == null || !params.getClass().equals(notificationHandler.getClass())) {
                    if (notificationHandler.type.isInstance(message)) {
                        params = message;
                    } else {
                        params = jsonHelper.jsonToObject(message.getParams(), notificationHandler.type);
                    }
                }
                notificationHandler.handler.onMessage(params);
                notificationHandler.handler.onFinished();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onResponse(JsonRpcResponse message) {
        MessageHandlerEntry messageHandler;
        synchronized (messageHandlers) {
            messageHandler = messageHandlers.remove(message.getId());
        }
        if (messageHandler != null) {
            if (messageHandler.timer != null) {
                messageHandler.timer.cancel();
            }
            Object params = null;
            if (messageHandler.type != null) {
                if (messageHandler.type.isInstance(message)) {
                    params = message;
                } else if (message.getResult() != null) {
                    params = jsonHelper.jsonToObject(message.getResult(), messageHandler.type);
                }
            }
            if (message.getError() != null) {
                messageHandler.handler.onError(message.getError().getCode(), message.getError().getMessage(), message.getError().getData());
            } else {
                messageHandler.handler.onMessage(params);
            }
            messageHandler.handler.onFinished();
            return true;
        } else {
            return false;
        }
    }

}
