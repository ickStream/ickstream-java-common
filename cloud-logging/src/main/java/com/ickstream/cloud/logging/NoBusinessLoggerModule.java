/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.ickstream.protocol.backend.common.BusinessCall;
import com.ickstream.protocol.backend.common.BusinessLogger;

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
            }

            @Override
            public void logFailed(BusinessCall businessCall, String error, Throwable exception) {
            }

            @Override
            public void logFailed(BusinessCall businessCall, Throwable exception) {
            }

            @Override
            public void logFailed(BusinessCall businessCall, String error) {
            }
        };
    }
}
