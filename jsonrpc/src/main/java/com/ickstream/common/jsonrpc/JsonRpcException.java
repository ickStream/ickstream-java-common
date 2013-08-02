/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * An exception representing a JSON-RPC error
 */
public class JsonRpcException extends Exception {
    private int code;
    private String data;

    /**
     * Create a new empty instance
     */
    public JsonRpcException() {
    }

    /**
     * Create a new instance
     *
     * @param code    The error code, typically one of the codes specified in {@link JsonRpcError}
     * @param message The error message describing the error
     * @param data    Additional data related to the error message
     */
    public JsonRpcException(int code, String message, String data) {
        super(code + ": " + message);
        this.code = code;
        this.data = data;
    }

    /**
     * Get the JSON-RPC error code, this ist typically one of the codes specified in {@link JsonRpcError}
     *
     * @return The JSON-RPC error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the additional JSON-RPC error data provided with the error
     *
     * @return The additional data provided with the error
     */
    public String getData() {
        return data;
    }
}
