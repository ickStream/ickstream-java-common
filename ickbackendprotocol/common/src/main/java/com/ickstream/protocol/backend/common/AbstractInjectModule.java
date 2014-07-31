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
