/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

/**
 * This interface represents a logging mechanism for access logging
 */
public interface BusinessLogger {
    /**
     * Log a successful request
     *
     * @param businessCall The {@link BusinessCall} object containing the information to log
     */
    void logSuccessful(BusinessCall businessCall);

    /**
     * Log a failed request
     *
     * @param businessCall The {@link BusinessCall} object containing the information to log
     * @param error        A textual error message describing the failure
     * @param exception    An exception which caused the failure
     */
    void logFailed(BusinessCall businessCall, String error, Throwable exception);

    /**
     * Log a failed request
     *
     * @param businessCall The {@link BusinessCall} object containing the information to log
     * @param exception    An exception which caused the failure
     */
    void logFailed(BusinessCall businessCall, Throwable exception);

    /**
     * Log a failed request
     *
     * @param businessCall The {@link BusinessCall} object containing the information to log
     * @param error        A textual error message describing the failure
     */
    void logFailed(BusinessCall businessCall, String error);
}
