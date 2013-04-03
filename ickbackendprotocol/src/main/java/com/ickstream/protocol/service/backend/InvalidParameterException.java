/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.backend;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
    }

    public InvalidParameterException(String parameter, String value) {
        super(parameter + "=" + value);
    }
}
