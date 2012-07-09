/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

public class JsonRpcException extends Exception {
    private int code;
    private String data;

    public JsonRpcException() {
    }

    public JsonRpcException(int code, String message, String data) {
        super(code +": "+message);
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getData() {
        return data;
    }
}
