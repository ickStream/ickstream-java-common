/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common.exception;

/**
 * Exception thrown when the service returns an error
 */
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
