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
