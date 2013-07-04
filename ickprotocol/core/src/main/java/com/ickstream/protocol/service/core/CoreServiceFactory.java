/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.ServiceFactory;

public class CoreServiceFactory extends ServiceFactory {
    private static final String CORESERVICE_ENDPOINT = "http://api.ickstream.com/ickstream-cloud-core/jsonrpc";
    private static final String PUBLICCORESERVICE_ENDPOINT = "http://api.ickstream.com/ickstream-cloud-core/public/jsonrpc";

    public static String getCoreServiceEndpoint() {
        String url = System.getProperty("ickstream-core-url");
        if (url != null) {
            return url;
        } else {
            return CORESERVICE_ENDPOINT;
        }
    }

    private static String getPublicEndpoint() {
        String url = System.getProperty("ickstream-core-public-url");
        if (url != null) {
            return url;
        } else {
            return PUBLICCORESERVICE_ENDPOINT;
        }
    }

    public static CoreService getCoreService(String accessToken, MessageLogger messageLogger) {
        return getCoreService(null, accessToken, messageLogger);
    }

    public static CoreService getCoreService(String cloudCoreUrl, String accessToken, MessageLogger messageLogger) {
        CoreService coreService = new CoreService(createHttpClient(), cloudCoreUrl == null ? getCoreServiceEndpoint() : cloudCoreUrl);
        coreService.setAccessToken(accessToken);
        coreService.setMessageLogger(messageLogger);
        return coreService;
    }

    public static PublicCoreService getPublicCoreService(String accessToken, MessageLogger messageLogger) {
        PublicCoreService publicCoreService = new PublicCoreService(createHttpClient(), getPublicEndpoint());
        publicCoreService.setAccessToken(accessToken);
        publicCoreService.setMessageLogger(messageLogger);
        return publicCoreService;
    }

    public static CoreService getCoreService(String accessToken) {
        return getCoreService(null, accessToken);
    }

    public static CoreService getCoreService(String cloudCoreUrl, String accessToken) {
        return getCoreService(cloudCoreUrl, accessToken, null);
    }

    public static PublicCoreService getPublicCoreService(String accessToken) {
        return getPublicCoreService(accessToken, null);
    }

}
