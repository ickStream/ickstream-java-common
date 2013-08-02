/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * A JSON-RPC request handler that's able to process received JSON-RPC requests or notifcations
 */
public interface JsonRpcRequestHandler {
    /**
     * Method that will be called when a JSON-RPC request or notification is received
     *
     * @param request The JSON-RPC request/notification received
     * @return true if the message has been handled, else false. If false is returned the processing will continue with other registered handlers
     */
    boolean onRequest(JsonRpcRequest request);
}