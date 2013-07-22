/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.ProtocolVersionsResponse;
import com.ickstream.protocol.service.corebackend.CoreBackendService;

public class CacheManagerService extends AbstractCloudService {
    @Override
    protected String getServiceId() {
        return "cache";
    }

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
