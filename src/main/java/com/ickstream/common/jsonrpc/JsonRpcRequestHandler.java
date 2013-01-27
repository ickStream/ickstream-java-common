/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

public interface JsonRpcRequestHandler {
    boolean onRequest(JsonRpcRequest request);
}