/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud;

public class ServiceException extends Exception {
    public ServiceException(int code, String message) {
        super("Error: " + code + ", Message: " + message);
    }
}
