/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.common.exception;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(int code, String message) {
        super(code, message);
    }
}
