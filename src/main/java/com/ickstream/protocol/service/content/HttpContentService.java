/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.jsonrpc.HttpMessageSender;
import com.ickstream.common.jsonrpc.MessageLogger;
import org.apache.http.client.HttpClient;
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
            return new DefaultHttpClient(new ThreadSafeClientConnManager());
        }
    }
}
