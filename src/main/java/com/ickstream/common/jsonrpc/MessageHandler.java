/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

/**
 * Interface for a message handler able to receive message responses or notifications. If you want to ensure
 * compatibility with future versions you should extend from {@link MessageHandlerAdapter} instead of implementing
 * this interface directly.
 *
 * @param <T> The type of data which the message handler will process
 */
public interface MessageHandler<T> {
    /**
     * Called when the response or notification is received and it's not an error
     *
     * @param message The actual message data
     */
    void onMessage(T message);

    /**
     * Called when an error of a request is received
     *
     * @param code    The JSON-RPC error code
     * @param message The JSON-RPC error message
     * @param data    The JSON-RPC error data
     */
    void onError(int code, String message, String data);

    /**
     * Called when a timeout occurs, if this method is called the {@link #onMessage(Object)} method will never be
     * called if the response is received after the onTimeout call has happened.
     */
    void onTimeout();

    /**
     * Called for each response or notification after either {@link #onMessage(Object)} or {@link #onTimeout()} or
     * {@link #onError(int, String, String)} has been processed completely. The typically usage of this method is to
     * perform clean-up needed after the processing is finished.
     */
    void onFinished();
}
