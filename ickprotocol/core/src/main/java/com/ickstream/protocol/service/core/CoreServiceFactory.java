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

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.ServiceFactory;

/**
 * Factory which should be used to get client classes for {@link CoreService} or {@link PublicCoreService}
 */
public class CoreServiceFactory extends ServiceFactory {
    private static final String CORESERVICE_ENDPOINT = ServiceFactory.getServer() + "/ickstream-cloud-core/jsonrpc";
    private static final String PUBLICCORESERVICE_ENDPOINT = ServiceFactory.getServer() + "/ickstream-cloud-core/public/jsonrpc";

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
