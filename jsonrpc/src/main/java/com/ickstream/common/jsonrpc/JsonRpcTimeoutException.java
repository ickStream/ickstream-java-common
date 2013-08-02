/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * Exception that indicates that a timeout occured when calling a JSON-RPC service
 */
public class JsonRpcTimeoutException extends Exception {
    /**
     * Creates a new instance
     */
    public JsonRpcTimeoutException() {
    }

    /**
     * Creates a new instance which was caused by the specified exception
     *
     * @param t The exception that caused the timeout to happen
     */
    public JsonRpcTimeoutException(Throwable t) {
    }
}
