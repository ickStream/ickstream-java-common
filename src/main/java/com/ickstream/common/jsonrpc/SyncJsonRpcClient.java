/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SyncJsonRpcClient extends AsyncJsonRpcClient {
    private Integer defaultTimeout = null;

    public SyncJsonRpcClient(MessageSender messageSender) {
        super(messageSender);
    }

    public SyncJsonRpcClient(MessageSender messageSender, Integer defaultTimeout) {
        super(messageSender, defaultTimeout);
        this.defaultTimeout = null;
    }

    private static class SynchronizedMessageHandler<T> implements MessageHandler<T> {
        private Semaphore semaphore;
        private List<T> results;
        private List<JsonRpcResponse.Error> errors;

        public SynchronizedMessageHandler(Semaphore semaphore, List<T> results, List<JsonRpcResponse.Error> errors) {
            this.semaphore = semaphore;
            this.results = results;
            this.errors = errors;
        }

        @Override
        public void onMessage(T message) {
            results.add(message);
            semaphore.release();
        }

        @Override
        public void onError(int code, String message, String data) {
            errors.add(new JsonRpcResponse.Error(code, message, data));
            semaphore.release();
        }

        @Override
        public void onTimeout() {
        }
    }

    protected <T> T sendRequest(String method, Object params, Class<T> messageResponseClass, Integer timeout) throws JsonRpcException, JsonRpcTimeoutException {
        List<T> results = new ArrayList<T>();
        List<JsonRpcResponse.Error> errors = new ArrayList<JsonRpcResponse.Error>();
        Semaphore semaphore = new Semaphore(0);
        MessageHandler<T> messageHandler = new SynchronizedMessageHandler<T>(semaphore, results, errors);

        String id = super.sendRequest(method, params, messageResponseClass, messageHandler);
        try {
            boolean acquired;
            if ((timeout == null && defaultTimeout == null) || (timeout != null && timeout < 0)) {
                semaphore.acquire();
                acquired = true;
            } else {
                acquired = semaphore.tryAcquire(timeout != null ? timeout : defaultTimeout, TimeUnit.MILLISECONDS);
            }
            if (acquired) {
                if (results.size() > 0) {
                    return results.get(0);
                } else {
                    if (errors.size() > 0 && errors.get(0) != null) {
                        throw new JsonRpcException(errors.get(0).getCode(), errors.get(0).getMessage(), errors.get(0).getData());
                    } else {
                        throw new JsonRpcException();
                    }
                }
            } else {
                removeMessageHandler(id);
                throw new JsonRpcTimeoutException();
            }
        } catch (InterruptedException e) {
            removeMessageHandler(id);
            throw new JsonRpcTimeoutException();
        }
    }

    protected <T> T sendRequest(String method, Object params, Class<T> messageResponseClass) throws JsonRpcException, JsonRpcTimeoutException {
        return sendRequest(method, params, messageResponseClass, (Integer) null);
    }
}
