/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class NoBusinessLoggerModule extends AbstractModule {
    @Override
    protected void configure() {
    }

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
