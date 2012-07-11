/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

public interface MessageHandler<T> {
    void onMessage(T message);

    void onError(int code, String message, String data);

    void onTimeout();
}
