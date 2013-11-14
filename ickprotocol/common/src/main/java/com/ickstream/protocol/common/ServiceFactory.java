/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common;

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

public class ServiceFactory {
    public static HttpClient createHttpClient() {
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
