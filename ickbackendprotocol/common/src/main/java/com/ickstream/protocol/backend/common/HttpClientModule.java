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
