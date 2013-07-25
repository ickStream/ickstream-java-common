/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;

/**
 * Injection module that provides instances of {@link HttpClient}, a service should use
 * {@link InjectHelper#instance(Class)} if it needs access to a {@link HttpClient} object
 */
public class HttpClientModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    /**
     * Provides {@link HttpClient} instance
     *
     * @return A {@link HttpClient} instance
     */
    @Provides
    @Singleton
    public HttpClient createHttpClient() {
        return new DefaultHttpClient(new PoolingClientConnectionManager());
    }
}
