/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.ServiceFactory;

/**
 * Factory which should be used to get client classes for {@link CoreService} or {@link PublicCoreService}
 */
public class CoreServiceFactory extends ServiceFactory {
    private static final String CORESERVICE_ENDPOINT = "https://api.ickstream.com/ickstream-cloud-core/jsonrpc";
    private static final String PUBLICCORESERVICE_ENDPOINT = "https://api.ickstream.com/ickstream-cloud-core/public/jsonrpc";

    /**
     * Get endpoint of Cloud Core service
     *
     * @return The endpoint URL of Cloud Core service
     */
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

    /**
     * Get a Cloud Core client and configure it to use the specified access token and message logger
     *
     * @param accessToken   The OAuth access token to use for authorization
     * @param messageLogger The message logger implementation to use for logging messages
     * @return A client for Cloud Core service
     */
    public static CoreService getCoreService(String accessToken, MessageLogger messageLogger) {
        return getCoreService(null, accessToken, messageLogger);
    }

    /**
     * Get a Cloud Core client and configure it to use the specified url, access token and message logger
     *
     * @param cloudCoreUrl  The endpoint URL of the Cloud Core service to use
     * @param accessToken   The OAuth access token to use for authorization
     * @param messageLogger The message logger implementation to use for logging messages
     * @return A client for Cloud Core service
     */
    public static CoreService getCoreService(String cloudCoreUrl, String accessToken, MessageLogger messageLogger) {
        CoreService coreService = new CoreService(createHttpClient(), cloudCoreUrl == null ? getCoreServiceEndpoint() : cloudCoreUrl);
        coreService.setAccessToken(accessToken);
        coreService.setMessageLogger(messageLogger);
        return coreService;
    }

    /**
     * Get a Cloud Core Authentication client and configure it to use the specified access token and message logger
     *
     * @param accessToken   The OAuth access token to use for authorization
     * @param messageLogger The message logger implementation to use for logging messages
     * @return A client for Cloud Core Authentication service
     */
    public static PublicCoreService getPublicCoreService(String accessToken, MessageLogger messageLogger) {
        PublicCoreService publicCoreService = new PublicCoreService(createHttpClient(), getPublicEndpoint());
        publicCoreService.setAccessToken(accessToken);
        publicCoreService.setMessageLogger(messageLogger);
        return publicCoreService;
    }

    /**
     * Get a Cloud Core client and configure it to use the specified access token
     *
     * @param accessToken The OAuth access token to use for authorization
     * @return A client for Cloud Core service
     */
    public static CoreService getCoreService(String accessToken) {
        return getCoreService(null, accessToken);
    }

    /**
     * Get a Cloud Core client and configure it to use the specified endpoint URl and access token
     *
     * @param cloudCoreUrl The endpoint URL of the Cloud Core service to use
     * @param accessToken  The OAuth access token to use for authorization
     * @return A client for Cloud Core service
     */
    public static CoreService getCoreService(String cloudCoreUrl, String accessToken) {
        return getCoreService(cloudCoreUrl, accessToken, null);
    }

    /**
     * Get a Cloud Core Authentication client and configure it to use the specified access token
     *
     * @param accessToken The OAuth access token to use for authorization
     * @return A client for Cloud Core Authentication service
     */
    public static PublicCoreService getPublicCoreService(String accessToken) {
        return getPublicCoreService(accessToken, null);
    }

}
