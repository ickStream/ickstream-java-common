/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

public interface JsonRpcResponseHandler {
    void onResponse(JsonRpcResponse response);
}
