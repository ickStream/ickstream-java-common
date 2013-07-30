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
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
        try {
            Integer defaultMaxPerRoute = Integer.valueOf(System.getProperty("ickstream-http-connections-max-per-route", "1000"));
            cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        } catch (NumberFormatException e) {
            System.err.println("ickstream-http-connections-max-per-route must be a number");
        }
        try {
            Integer maxTotal = Integer.valueOf(System.getProperty("ickstream-http-connections-max-total", "2000"));
            cm.setMaxTotal(maxTotal);
        } catch (NumberFormatException e) {
            System.err.println("ickstream-http-connections-max-total must be a number");
        }
        return new DefaultHttpClient(cm);
    }
}
