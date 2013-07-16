/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.sf.ehcache.Cache;

public class NoCacheManagerModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public CacheManager createCacheManager() {
        return new CacheManager() {
            @Override
            public Cache get(Class cls) {
                return null;
            }

            @Override
            public Cache get(String name) {
                return null;
            }
        };
    }
}
