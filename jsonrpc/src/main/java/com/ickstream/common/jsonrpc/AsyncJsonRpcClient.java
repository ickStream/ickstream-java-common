/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.node.ValueNode;

import java.util.*;

/**
 * Asynchronous client class for JSON-RPC requests. This class can be used independent of communication protocol
 * as the actual sending is done with a separate {@link MessageSender} implementation. If a synchronous behavior
 * is wanted, use the {@link SyncJsonRpcClient} class instead.
 */
public class AsyncJsonRpcClient implements JsonRpcRequestHandler, JsonRpcResponseHandler {
    private MessageSender messageSender;
    private JsonHelper jsonHelper = new JsonHelper();
    private final Map<String, MessageHandlerEntry> messageHandlers = new HashMap<String, MessageHandlerEntry>();
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

    /**
     * Creates a new instance which uses the specified message sender class to send messages.
     * The created instance will use {@link GlobalIdProvider} to generate unique identities for the
     * JSON-RPC messages
     *
     * @param messageSender The message sender implementation which should be used to send messages
     */
    public AsyncJsonRpcClient(MessageSender messageSender) {
        this(messageSender, null, null);
    }

    /**
     * Creates a new instance which uses th specified message sender and identity provider implementations
     *
     * @param messageSender The message sender implementation which should be used to send messages
     * @param idProvider    The identity provider implementation which should be use to generate JSON-RPC identities
     */
    public AsyncJsonRpcClient(MessageSender messageSender, IdProvider idProvider) {
        this(messageSender, idProvider, null);
    }

    /**
     * Creates a new instance which uses the specified message sender implementation and provides a default timeout
     * for all calls.
     * The created instance will use {@link GlobalIdProvider} to generate unique identities for the
     * JSON-RPC messages
     *
     * @param messageSender  The message sender implementation which should be used to send messages
     * @param defaultTimeout The default timeout which should be used in calls unless a specific timeout has been specified
     */
    public AsyncJsonRpcClient(MessageSender messageSender, Integer defaultTimeout) {
        this(messageSender, null, defaultTimeout);
    }

    /**
     * Creates a new instance which uses th specified message sender and identity provider implementations, it also
     * allows you to specify a default timeout for all calls
     *
     * @param messageSender  The message sender implementation which should be used to send messages
     * @param idProvider     The identity provider implementation which should be use to generate JSON-RPC identities
     * @param defaultTimeout The default timeout which should be used in calls unless a specific timeout has been specified
     */
    public AsyncJsonRpcClient(MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
        this.messageSender = messageSender;
        this.defaultTimeout = defaultTimeout;
        if (idProvider == null) {
            this.idProvider = new GlobalIdProvider();
        } else {
            this.idProvider = idProvider;
        }
    }

    /**
     * Ger message sender implementation used
     *
     * @return The message sender implementation used
     */
    protected MessageSender getMessageSender() {
        return messageSender;
    }

    /**
     * Send a JSON-RPC request using the specified method and parameters, this method will just ignore the answer to
     * the request so it should only be used for notifications and messages where the response can be ignored
     *
     * @param method The method to call
     * @param params The parameters to the method, this must be possible to serialize to JSON with the {@link JsonHelper} class
     * @return The identity for the request
     */
    public ValueNode sendRequest(String method, Object params) {
        return sendRequest(method, params, null, null);
    }

    /**
     * Send a JSON-RPC request using the specified method and parameters.
     * Also provide a callback which should handle the response when it's received asynchronously in the future.
     *
     * @param method               The method to call
     * @param params               The parameters to the method, this must be possible to serialize to JSON with the {@link JsonHelper} class
     * @param messageResponseClass The type of the response, must have a default constructor and must be possible to instantiate from JSON using {@link JsonHelper}
     * @param messageHandler       The message handler that will be called when a timeout occurs or the answer is received
     * @return The identity for the request
     */
    public ValueNode sendRequest(String method, Object params, Class messageResponseClass, MessageHandler messageHandler) {
        return sendRequest(method, params, messageResponseClass, messageHandler, null);
    }

    /**
     * Send a JSON-RPC request using the specified method and parameters.
     * Also provide a callback which should handle the response when it's received asynchronously in the future.
     * A timeout can be specified and this will be triggered if no response have been recevied within this time.
     *
     * @param method               The method to call
     * @param params               The parameters to the method, this must be possible to serialize to JSON with the {@link JsonHelper} class
     * @param messageResponseClass The type of the response, must have a default constructor and must be possible to instantiate from JSON using {@link JsonHelper}
     * @param messageHandler       The message handler that will be called when a timeout occurs or the answer is received
     * @param timeout              The timeout in millisecond, if not specified the default timeout will be used
     * @return The identity for the request
     */
    public ValueNode sendRequest(String method, Object params, Class messageResponseClass, MessageHandler messageHandler, Integer timeout) {
        final ValueNode id;
        synchronized (messageHandlers) {
            id = idProvider.getNextId();
            if (messageResponseClass != null && messageHandler != null) {
                final String idAsText = id.asText();
                if ((timeout == null && defaultTimeout == null) || (timeout != null && timeout < 0)) {
                    messageHandlers.put(idAsText, new MessageHandlerEntry(messageResponseClass, messageHandler));
                } else {
                    messageHandlers.put(idAsText, new MessageHandlerEntry(messageResponseClass, messageHandler, new Timer()));
                    messageHandlers.get(idAsText).timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            MessageHandlerEntry entry;
                            synchronized (messageHandlers) {
                                entry = messageHandlers.remove(idAsText);
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
        jsonRpcRequest.setId(id);
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

    /**
     * Add a listener that will be called when a specific notification is received
     *
     * @param method               The method name of the notification
     * @param messageParameterType The type of the parameter data, this must be a class with a default constructor which can be instantiated from JSON using {@link JsonHelper}
     * @param messageHandler       The implementation of the listener, this is the class that will be called when the notification is received
     * @param <T>                  The type of the parameter data, , this must be a class with a default constructor which can be instantiated from JSON using {@link JsonHelper}
     */
    public <T> void addNotificationListener(String method, Class<T> messageParameterType, MessageHandler<T> messageHandler) {
        synchronized (notificationHandlers) {
            if (!notificationHandlers.containsKey(method)) {
                notificationHandlers.put(method, new ArrayList<MessageHandlerEntry>());
            }
            notificationHandlers.get(method).add(new MessageHandlerEntry(messageParameterType, messageHandler));
        }
    }

    /**
     * Removes the message handler for the specified JSON-RPC request
     *
     * @param id The identity of the JSON-RPC request to remove the message handler for
     */
    protected void removeMessageHandler(ValueNode id) {
        synchronized (messageHandlers) {
            messageHandlers.remove(id.asText());
        }
    }

    /**
     * Removes a notification listener
     *
     * @param method         The method which the listener subscribed to
     * @param messageHandler The instance of the listener, this must be the same instance as previously used in {@link #addNotificationListener(String, Class, MessageHandler)}
     * @param <T>            The type of the parameter data, , this must be a class with a default constructor which can be instantiated from JSON using {@link JsonHelper}
     */
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

    /**
     * Process a received JSON-RPC notification by calling any notification handler that has been previously registered
     * with {@link #addNotificationListener(String, Class, MessageHandler)}.
     *
     * @param message The received JSON-RPC message
     * @return true if the message was forwarded to at least one notification handler
     */
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

    /**
     * Process a received JSON-RPC message response, correlate it with previously sent requests using the identity and
     * issue a call to the message handler callback that was specified when the request was sent using {@link #sendRequest(String, Object, Class, MessageHandler)} or
     * {@link #sendRequest(String, Object, Class, MessageHandler, Integer)}. If the message was sent using {@link #sendRequest(String, Object)} no callback will exist
     * and in this case this method will not process the message
     *
     * @param message The received JSON-RPC message response
     * @return true if the message is forwarded to a message handler, else false.
     */
    @Override
    public boolean onResponse(JsonRpcResponse message) {
        MessageHandlerEntry messageHandler;
        synchronized (messageHandlers) {
            messageHandler = messageHandlers.remove(message.getId().asText());
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
