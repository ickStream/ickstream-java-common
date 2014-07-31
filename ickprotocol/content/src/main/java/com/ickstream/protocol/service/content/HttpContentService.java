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

package com.ickstream.protocol.service.content;

import com.ickstream.common.jsonrpc.HttpMessageSender;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.IckStreamTrustManager;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

import javax.net.ssl.SSLContext;

/**
 * A HTTP POST based client used to communicate with online content services using the Content Access protocol.
 */
public class HttpContentService extends ContentService {

    /**
     * Creates a new instance for the specified service identity and HTTP endpoint url
     *
     * @param id       The service identity to communicate with
     * @param endpoint The service endpoint HTTP URL to use when communicating with the service
     */
    public HttpContentService(String id, String endpoint) {
        super(id, new HttpMessageSender(createHttpClient(), endpoint, true));
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    /**
     * Creates a new instance for the specified service identity, HTTP endpoint url and access token
     *
     * @param id          The service identity to communicate with
     * @param endpoint    The service endpoint HTTP URL to use when communicating with the service
     * @param accessToken The OAuth access token to use for authorization
     */
    public HttpContentService(String id, String endpoint, String accessToken) {
        this(id, endpoint);
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    /**
     * Set message logger to use when logging outgoing and incomming messages
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    /**
     * Set the OAuth access token to use for authorization
     *
     * @param accessToken An OAuth access token
     */
    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    private static HttpClient createHttpClient() {
        try {
            Class.forName("org.apache.http.impl.conn.PoolingClientConnectionManager");
            SSLContext sslContext = IckStreamTrustManager.getContext();
            if (sslContext != null) {
                SchemeRegistry scheme = SchemeRegistryFactory.createDefault();
                scheme.register(new Scheme("https", 443, new SSLSocketFactory(sslContext)));
                return new DefaultHttpClient(new PoolingClientConnectionManager(scheme));
            } else {
                return new DefaultHttpClient(new PoolingClientConnectionManager());
            }
        } catch (ClassNotFoundException e) {
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            return new DefaultHttpClient(new ThreadSafeClientConnManager(client.getParams(), registry), client.getParams());
        }
    }
}
