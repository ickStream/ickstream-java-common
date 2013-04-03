/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoBusinessLoggerQueryModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public BusinessLoggerQuery createBusinessLoggerQuery() {
        return new BusinessLoggerQuery() {
            @Override
            public List<BusinessLoggerEntry> find(String userId, String excludeUserId, Boolean excludeAnonymousAccess, String deviceId, String serviceId, String servicePrefix, String servicePostfix, String excludeServicePrefix, String excludeServicePostfix, String method, Long minimumDuration, Date beforeTimestamp, Boolean onlyErrors, Integer count) {
                return new ArrayList<BusinessLoggerEntry>();
            }
        };
    }
}
