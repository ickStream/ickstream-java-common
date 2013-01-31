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

public class HttpContentService extends ContentService {

    public HttpContentService(String id, String endpoint) {
        super(id, new HttpMessageSender(createHttpClient(), endpoint, true));
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public HttpContentService(String id, String endpoint, String accessToken) {
        this(id, endpoint);
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

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
