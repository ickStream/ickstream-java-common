/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Injection module that provides a dummy {@link BusinessLogger} which doesn't do any logging
 * <p/>
 * This module is automatically loaded by {@link AbstractInjectServletConfig} unless overridden
 */
public class NoBusinessLoggerModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    /**
     * Provides a dummy {@link BusinessLogger} which doesn't do any logging
     *
     * @return A {@link BusinessLogger} instance
     */
    @Provides
    @Singleton
    public BusinessLogger createBusinessLogger() {
        return new BusinessLogger() {
            @Override
            public void logSuccessful(BusinessCall businessCall) {
                // Do nothing
            }

            @Override
            public void logFailed(BusinessCall businessCall, String error, Throwable exception) {
                // Do nothing
            }

            @Override
            public void logFailed(BusinessCall businessCall, Throwable exception) {
                // Do nothing
            }

            @Override
            public void logFailed(BusinessCall businessCall, String error) {
                // Do nothing
            }
        };
    }
}
