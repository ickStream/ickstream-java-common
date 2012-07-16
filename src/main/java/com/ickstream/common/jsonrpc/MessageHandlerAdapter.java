/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

/**
 * Adapter class for the {@link MessageHandler} interface, implement your message handlers by extending
 * this class to achieve compatibility with future versions fo the {@link MessageHandler} interface
 *
 * @param <T>
 */
public class MessageHandlerAdapter<T> implements MessageHandler<T> {
    @Override
    public void onMessage(T message) {
        //Do nothing
    }

    @Override
    public void onError(int code, String message, String data) {
        System.err.println("An error was returned: " + code + (message != null ? ":" + message : "") + (data != null ? "\n" + data : ""));
    }

    @Override
    public void onTimeout() {
        System.err.println("An operation was timed out");
    }

    @Override
    public void onFinished() {
        // Do nothing
    }
}
