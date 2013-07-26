/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.corebackend.DeviceResponse;
import net.sf.ehcache.Cache;

/**
 * Cache manager that provides caches needed for {@link com.ickstream.protocol.service.corebackend.CoreBackendService}
 * access
 * <p/>
 * Should instead ensure {@link CacheManagerModule} is loaded and after that you can in the service use
 * {@link InjectHelper#instance(Class)} to get the current instance of the CacheManager.
 */
public class CoreBackendCacheManager extends CacheManager {
    public CoreBackendCacheManager() {
    }

    /**
     * Get cache for the specific class, it will only return caches needed for {@link com.ickstream.protocol.service.corebackend.CoreBackendService} access
     *
     * @param cls The class to get a cache for
     * @return The {@link Cache} object representing the cache or null if no cache is available for the specified class
     */
    public Cache get(Class cls) {
        if (cls.getPackage().getName().equals(DeviceResponse.class.getPackage().getName())) {
            return super.get(cls);
        } else {
            return null;
        }
    }

    /**
     * Get cache for the specific name, it will only return caches needed for {@link com.ickstream.protocol.service.corebackend.CoreBackendService} access
     *
     * @param name The name of the cache
     * @return The {@link Cache} object representing the cache or null if no cache is available for the specified name
     */
    public Cache get(String name) {
        if (name.startsWith(DeviceResponse.class.getPackage().getName())) {
            return super.get(name);
        } else {
            return null;
        }
    }
}
