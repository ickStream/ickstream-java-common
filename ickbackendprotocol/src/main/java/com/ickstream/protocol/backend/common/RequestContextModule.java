/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;

public class RequestContextModule extends AbstractModule {
    @Override
    protected void configure() {

    }

    @Provides
    @RequestScoped
    public RequestContext getRequestContext() {
        return new RequestContext();
    }
}
