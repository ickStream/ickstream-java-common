/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.ProtocolVersionsResponse;
import com.ickstream.protocol.service.corebackend.CoreBackendService;

/**
 * A service which implements a "clearCache" method which can be used to clear all caches handled by the
 * {@link CacheManager} used by this service
 * <p/>
 * This service should is primarily useful during testing where one might want to clear the cache without restarting
 * the web server
 */
public class CacheManagerService extends AbstractCloudService {
    @Override
    protected CoreBackendService getCoreBackendService() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ProtocolVersionsResponse getProtocolVersions() {
        throw new RuntimeException("Not implemented");
    }


    public Boolean clearCache() {
        InjectHelper.instance(CacheManager.class).clearAll();
        return true;
    }
}
