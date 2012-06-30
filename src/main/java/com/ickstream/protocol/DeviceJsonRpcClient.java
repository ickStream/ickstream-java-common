/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import com.ickstream.protocol.cloud.ServerException;
import com.ickstream.protocol.device.MessageSender;
import com.ickstream.protocol.device.player.MessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class DeviceJsonRpcClient extends JsonRpcClient implements JsonRpcResponseHandler {
    private String serviceId;
    private MessageSender messageSender;
    private final Map<Integer, Semaphore> responseSemaphores = new HashMap<Integer, Semaphore>();
    private final Map<Integer, Class> messageHandlerTypes = new HashMap<Integer, Class>();
    private final Map<Integer, MessageHandler> messageHandlers = new HashMap<Integer, MessageHandler>();

    public DeviceJsonRpcClient(String serviceId, MessageSender messageSender) {
        this.serviceId = serviceId;
        this.messageSender = messageSender;
    }

    @Override
    public <T> T callMethod(String method, Object parameters, Class<T> responseClass) throws ServerException {
        final List<T> results = new ArrayList<T>();
        final Semaphore semaphore = new Semaphore(0);
        MessageHandler<T> messageHandler = new MessageHandler<T>() {
            @Override
            public void onMessage(T message) {
                results.add(message);
                semaphore.release();
            }
        };

        synchronized (messageHandlers) {
            Integer id = getNextId();
            if (responseClass != null) {
                messageHandlers.put(id, messageHandler);
                messageHandlerTypes.put(id, responseClass);
                responseSemaphores.put(id,semaphore);
            }
            JsonRpcRequest request = new JsonRpcRequest();
            request.setMethod(method);
            request.setId(id);
            if (parameters != null) {
                request.setParams(getJsonHelper().objectToJson(parameters));
            }
            String requestString = getJsonHelper().objectToString(request);
            System.out.println("SENDING Service REQUEST (" + serviceId + "):\n" + requestString + "\n");
            messageSender.sendMessage(serviceId, requestString);
        }
        try {
            semaphore.acquire();
            return results.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResponse(JsonRpcResponse message) {
        synchronized (messageHandlers) {
            MessageHandler messageHandler = messageHandlers.get(message.getId());
            Class responseType = messageHandlerTypes.get(message.getId());
            if (messageHandler != null) {
                Object parameters = null;
                if (responseType != null) {
                    parameters = getJsonHelper().jsonToObject(message.getResult(), responseType);
                }
                messageHandlers.get(message.getId()).onMessage(parameters);
                messageHandlers.remove(message.getId());
                messageHandlerTypes.remove(message.getId());
                responseSemaphores.remove(message.getId());
            }
        }
    }
}
