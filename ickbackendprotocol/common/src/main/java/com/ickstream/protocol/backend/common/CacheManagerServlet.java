/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.Singleton;

@Singleton
public class CacheManagerServlet extends AbstractCloudServlet {
    public CacheManagerServlet() {
        super(CacheManagerService.class, CacheManagerService.class);
    }
}
