/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

public interface BusinessLogger {
    void logSuccessful(BusinessCall businessCall);

    void logFailed(BusinessCall businessCall, String error, Throwable exception);

    void logFailed(BusinessCall businessCall, Throwable exception);

    void logFailed(BusinessCall businessCall, String error);
}
