/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common.exception;

/**
 * Exception thrown when the service doesn't respond within the defined timeout
 */
public class ServiceTimeoutException extends Exception {
    public ServiceTimeoutException() {
    }

    public ServiceTimeoutException(Throwable t) {
    }
}
