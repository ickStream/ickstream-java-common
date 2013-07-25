/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.Singleton;

/**
 * Servlet which expose the {@link CacheManagerService} JSON-RPC interface
 */
@Singleton
public class CacheManagerServlet extends AbstractCloudServlet {
    public CacheManagerServlet() {
        super(CacheManagerService.class, CacheManagerService.class);
    }
}
