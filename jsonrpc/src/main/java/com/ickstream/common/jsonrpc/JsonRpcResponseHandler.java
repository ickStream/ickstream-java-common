/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * A JSON-RPC response handler that's able to process received JSON-RPC responses
 */
public interface JsonRpcResponseHandler {
    /**
     * Method that will be called when a JSON-RPC response is received
     *
     * @param response The JSON-RPC response received
     * @return true if the message has been handled, else false. If false is returned the processing will continue with other registered handlers
     */
    boolean onResponse(JsonRpcResponse response);
}