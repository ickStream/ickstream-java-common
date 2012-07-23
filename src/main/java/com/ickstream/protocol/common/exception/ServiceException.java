/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.common.exception;

public class ServiceException extends Exception {
    private int code;

    public ServiceException(int code, String message) {
        super("Error: " + code + ", Message: " + message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
