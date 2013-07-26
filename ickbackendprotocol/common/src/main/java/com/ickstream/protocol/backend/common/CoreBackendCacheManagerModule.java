/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Injection module that provides instances of {@link CacheManager} for caching needed for
 * {@link com.ickstream.protocol.service.corebackend.CoreBackendService} access
 */
public class CoreBackendCacheManagerModule extends AbstractModule {
    public CoreBackendCacheManagerModule() {
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public CacheManager createCacheManager() {
        return new CoreBackendCacheManager();
    }
}
