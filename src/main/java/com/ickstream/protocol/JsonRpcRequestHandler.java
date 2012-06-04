/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

public interface JsonRpcRequestHandler {
    void onRequest(JsonRpcRequest request);
}
