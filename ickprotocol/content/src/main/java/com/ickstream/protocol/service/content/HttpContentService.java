/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.jsonrpc.HttpMessageSender;
import com.ickstream.common.jsonrpc.MessageLogger;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

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
            return new DefaultHttpClient(new PoolingClientConnectionManager());
        } catch (ClassNotFoundException e) {
            DefaultHttpClient client = new DefaultHttpClient();
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            return new DefaultHttpClient(new ThreadSafeClientConnManager(client.getParams(), registry), client.getParams());
        }
    }
}
