/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;

/**
 * Abstract class which provides the typical injector modules needed to instansiate objects.
 */
public abstract class AbstractInjectModule extends ServletModule {
    private Class<? extends HttpServlet> cloudServiceClass;
    private Class<? extends Filter> authorizationFilter;

    /**
     * Creates a new instance which configures the web application and connects appropriate URL's to the service
     *
     * @param cloudServletClass   The servlet which implements the JSON-RPC interface of the service, typically this is a sub class of {@link AbstractCloudServlet}
     * @param authorizationFilter The filter which implements the authentication of the JSON-RPC interface of the service, typically this is a sub class of {@link AbstractOAuthAuthorizationFilter}
     */
    public AbstractInjectModule(Class<? extends HttpServlet> cloudServletClass, Class<? extends Filter> authorizationFilter) {
        this.cloudServiceClass = cloudServletClass;
        this.authorizationFilter = authorizationFilter;
    }

    /**
     * Configure the cloud servlet class and authentication filter specified in
     * {@link #AbstractInjectModule(Class, Class)} so they can be accessed through /jsonrpc.
     * Also installs any standard injection modules needed in a typical service, this includes modules to
     * inject {@link UriResolver}, {@link org.apache.http.client.HttpClient}, {@link RequestContext} and
     * {@link BackendRequestContext}
     */
    protected void configureServlets() {

        for (AbstractModule module : InjectHelper.getModules()) {
            install(module);
        }
        if (authorizationFilter != null) {
            filter("/jsonrpc").through(authorizationFilter);
            filter("/cache").through(authorizationFilter);
        }
        serve("/jsonrpc").with(cloudServiceClass);
        serve("/cache").with(CacheManagerServlet.class);
    }
}
