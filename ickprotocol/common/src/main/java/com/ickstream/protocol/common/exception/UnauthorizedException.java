/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common.exception;

/**
 * Exception thrown when the service rejects the message due to unauthorized access
 */
public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(int code, String message) {
        super(code, message);
    }
}
