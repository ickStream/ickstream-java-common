/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common.exception;

public class UnauthorizedException extends ServiceException {
    public UnauthorizedException(int code, String message) {
        super(code, message);
    }
}
