/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import net.sf.ehcache.Cache;

/**
 * Cache manager that provides caches which can be used to cache information in the service
 * <p/>
 * To activate it, you need to ensure {@link CacheManagerModule} is loaded and after that you can in the service use
 * {@link InjectHelper#instance(Class)} to get the current instance of the CacheManager.
 */
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

    /**
     * Get cache for the specific class
     *
     * @param cls The class to get a cache for
     * @return The {@link Cache} object representing the cache or null if no cache is available for the specified class
     */
    public Cache get(Class cls) {
        return get(cls.getSimpleName());
    }

    /**
     * Get cache for the specific name
     *
     * @param name The name of the cache
     * @return The {@link Cache} object representing the cache or null if no cache is available for the specified name
     */
    public Cache get(String name) {
        Cache cache = getCacheManager().getCache(name);
        if (cache == null) {
            getCacheManager().addCache(name);
            cache = getCacheManager().getCache(name);
        }
        return cache;
    }

    /**
     * Clear all caches managed by the cache manager
     */
    public void clearAll() {
        getCacheManager().clearAll();
    }
}
