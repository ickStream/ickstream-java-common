/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.common;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.core.CoreService;
import com.ickstream.protocol.service.core.FindServicesRequest;
import com.ickstream.protocol.service.core.FindServicesResponse;
import com.ickstream.protocol.service.library.LibraryService;
import com.ickstream.protocol.service.scrobble.ScrobbleService;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

public class ServiceFactory {
    private static final String CORESERVICE_ENDPOINT = "http://api.ickstream.com/ickstream-cloud-core/jsonrpc";

    private static String getEndpoint() {
        String url = System.getProperty("ickstream-core-url");
        if (url != null) {
            return url;
        } else {
            return CORESERVICE_ENDPOINT;
        }
    }

    public static CoreService getCoreService(String accessToken, MessageLogger messageLogger) {
        CoreService coreService = new CoreService(createHttpClient(), getEndpoint());
        coreService.setAccessToken(accessToken);
        coreService.setMessageLogger(messageLogger);
        return coreService;
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

    public static CoreService getCoreService(String accessToken) {
        return getCoreService(accessToken, null);
    }

    public static ScrobbleService getScrobbleService(String accessToken, MessageLogger messageLogger) {
        try {
            FindServicesResponse response = getCoreService(accessToken, messageLogger).findServices(new FindServicesRequest("scrobble"));
            if (response != null && response.getItems().size() > 0) {
                ScrobbleService scrobbleService = new ScrobbleService(new DefaultHttpClient(), response.getItems().get(0).getUrl());
                scrobbleService.setAccessToken(accessToken);
                scrobbleService.setMessageLogger(messageLogger);
                return scrobbleService;
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ServiceTimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ScrobbleService getScrobbleService(String accessToken) {
        return getScrobbleService(accessToken, null);
    }

    public static LibraryService getLibraryService(String accessToken, MessageLogger messageLogger) {
        try {
            FindServicesResponse response = getCoreService(accessToken, messageLogger).findServices(new FindServicesRequest("librarymanagement"));
            if (response != null && response.getItems().size() > 0) {
                LibraryService libraryService = new LibraryService(new DefaultHttpClient(), response.getItems().get(0).getUrl());
                libraryService.setAccessToken(accessToken);
                libraryService.setMessageLogger(messageLogger);
                return libraryService;
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        } catch (ServiceTimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LibraryService getLibraryService(String accessToken) {
        return getLibraryService(accessToken, null);
    }
}
