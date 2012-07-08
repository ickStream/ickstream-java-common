/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SyncJsonRpcClient extends AsyncJsonRpcClient {
    public SyncJsonRpcClient(MessageSender messageSender) {
        super(messageSender);
    }

    private static class SynchronizedMessageHandler<T> implements MessageHandler<T> {
        private Semaphore semaphore;
        private List<T> results;

        public SynchronizedMessageHandler(Semaphore semaphore, List<T> results) {
            this.semaphore = semaphore;
            this.results = results;
        }

        @Override
        public void onMessage(T message) {
            results.add(message);
            semaphore.release();
        }
    }

    protected <T> T sendRequest(String method, Object params, Class<T> messageResponseClass) {
        List<T> results = new ArrayList<T>();
        Semaphore semaphore = new Semaphore(0);
        MessageHandler<T> messageHandler = new SynchronizedMessageHandler<T>(semaphore, results);

        super.sendRequest(method, params, messageResponseClass, messageHandler);
        try {
            semaphore.acquire();
            return results.get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
