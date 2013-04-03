/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

public abstract class AbstractInjectModule extends ServletModule {
    private Class<? extends AbstractCloudServlet> cloudServiceClass;
    private Class<? extends AbstractOAuthAuthorizationFilter> authorizationFilter;

    public AbstractInjectModule(Class<? extends AbstractCloudServlet> cloudServiceClass, Class<? extends AbstractOAuthAuthorizationFilter> authorizationFilter) {
        this.cloudServiceClass = cloudServiceClass;
        this.authorizationFilter = authorizationFilter;
    }

    protected void configureServlets() {

        for (AbstractModule module : InjectHelper.getModules()) {
            install(module);
        }
        if (authorizationFilter != null) {
            filter("/jsonrpc").through(authorizationFilter);
        }
        serve("/jsonrpc").with(cloudServiceClass);
    }
}
