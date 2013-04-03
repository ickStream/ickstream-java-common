/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import net.sf.ehcache.Cache;

public class CacheManager {
    net.sf.ehcache.CacheManager cacheManager;

    public CacheManager() {
    }

    private synchronized net.sf.ehcache.CacheManager getCacheManager() {
        if (cacheManager == null) {
            cacheManager = net.sf.ehcache.CacheManager.create();
        }
        return cacheManager;
    }

    public Cache get(Class cls) {
        return get(cls.getSimpleName());
    }

    public Cache get(String name) {
        Cache cache = getCacheManager().getCache(name);
        if (cache == null) {
            getCacheManager().addCache(name);
            cache = getCacheManager().getCache(name);
        }
        return cache;
    }
}
