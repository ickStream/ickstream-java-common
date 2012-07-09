/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

public class MessageHandlerAdapter<T> implements MessageHandler<T> {
    @Override
    public void onMessage(T message) {
        //Do nothing
    }

    @Override
    public void onError(int code, String message, String data) {
        throw new RuntimeException(new JsonRpcException(code, message, data));
    }
}
