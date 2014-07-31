/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.corebackend.DeviceResponse;
import com.ickstream.protocol.service.corebackend.UserServiceResponse;
import net.sf.ehcache.Cache;
import net.sf.ehcache.config.CacheConfiguration;

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

            cacheManager.addCache(DeviceResponse.class.getName());
            Cache cache = cacheManager.getCache(DeviceResponse.class.getName());
            CacheConfiguration configuration = cache.getCacheConfiguration();
            configuration.setMaxEntriesLocalHeap(1000);
            configuration.setEternal(false);
            configuration.setTimeToIdleSeconds(60);
            configuration.setTimeToLiveSeconds(60);
            configuration.setMaxEntriesLocalDisk(0);

            cacheManager.addCache(UserServiceResponse.class.getName());
            cache = cacheManager.getCache(UserServiceResponse.class.getName());
            configuration = cache.getCacheConfiguration();
            configuration.setMaxEntriesLocalHeap(1000);
            configuration.setEternal(false);
            configuration.setTimeToIdleSeconds(60);
            configuration.setTimeToLiveSeconds(60);
            configuration.setMaxEntriesLocalDisk(0);
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
        return get(cls.getName());
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
