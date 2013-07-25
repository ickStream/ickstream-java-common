/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Injection module that provides instances of {@link CacheManager}
 */
public class CacheManagerModule extends AbstractModule {
    public CacheManagerModule() {
    }

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public CacheManager createCacheManager() {
        return new CacheManager();
    }
}
