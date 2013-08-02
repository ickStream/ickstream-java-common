/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.scrobble;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.core.CoreServiceFactory;
import com.ickstream.protocol.service.core.FindServicesRequest;
import com.ickstream.protocol.service.core.FindServicesResponse;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Factory which should be used to get client classes for {@link ScrobbleService}
 */
public class ScrobbleServiceFactory extends CoreServiceFactory {
    /**
     * Get a Scrobble service client and configure it to use the specified url, access token and message logger.
     * The service URL will be detected by calling Cloud Core service.
     *
     * @param cloudCoreUrl  The endpoint URL of the Cloud Core service to use
     * @param accessToken   The OAuth access token to use for authorization
     * @param messageLogger The message logger implementation to use for logging messages
     * @return A client for Scrobble service
     */
    public static ScrobbleService getScrobbleService(String cloudCoreUrl, String accessToken, MessageLogger messageLogger) {
        try {
            FindServicesResponse response = getCoreService(cloudCoreUrl, accessToken, messageLogger).findServices(new FindServicesRequest("scrobble"));
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

    /**
     * Get a Scrobble service client and configure it to use the specified url and access token
     * The service URL will be detected by calling Cloud Core service.
     *
     * @param cloudCoreUrl The endpoint URL of the Cloud Core service to use
     * @param accessToken  The OAuth access token to use for authorization
     * @return A client for Scrobble service
     */
    public static ScrobbleService getScrobbleService(String cloudCoreUrl, String accessToken) {
        return getScrobbleService(cloudCoreUrl, accessToken, null);
    }
}
