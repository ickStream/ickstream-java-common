/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

/**
 * Exception which should be thrown when an invalid parameter value has been sent to a service
 */
public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException() {
    }

    public InvalidParameterException(String parameter, String value) {
        super(parameter + "=" + value);
    }
}
