/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.core.CoreServiceFactory;
import com.ickstream.protocol.service.core.FindServicesRequest;
import com.ickstream.protocol.service.core.FindServicesResponse;
import com.ickstream.protocol.service.core.ServiceResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory which should be used to get client classes for {@link ContentService}
 */
public class ContentServiceFactory extends CoreServiceFactory {
    private static final Map<String, ContentService> contentServices = new HashMap<String, ContentService>();
    private static long lastRefreshedCloudServices = 0;

    /**
     * Register a content service in the factory so it's cached and quick to access next time.
     * This method must be called for local content services
     *
     * @param contentService The content service to add to the factory cache
     */
    public static void addContentService(ContentService contentService) {
        synchronized (contentServices) {
            contentServices.put(contentService.getId(), contentService);
        }
    }

    /**
     * Get a Scrobble service client and configure it to use the specified url, access token and message logger.
     * The service URL will be detected by calling Cloud Core service.
     *
     * @param cloudCoreUrl  The endpoint URL of the Cloud Core service to use
     * @param accessToken   The OAuth access token to use for authorization
     * @param messageLogger The message logger implementation to use for logging messages
     * @return A client for Scrobble service
     */
    public static ContentService getContentService(String serviceId, String cloudCoreUrl, String accessToken, MessageLogger messageLogger) {
        // We always prefer cached services if they exists
        synchronized (contentServices) {
            if (contentServices.containsKey(serviceId)) {
                return contentServices.get(serviceId);
            }
        }
        // Refresh cache every 5'th minute
        if (lastRefreshedCloudServices < (System.currentTimeMillis() - (1000 * 60 * 5))) {
            try {
                FindServicesResponse response = getCoreService(cloudCoreUrl, accessToken, messageLogger).findServices(new FindServicesRequest("content"));
                lastRefreshedCloudServices = System.currentTimeMillis();
                if (response != null && response.getItems().size() > 0) {
                    for (ServiceResponse serviceResponse : response.getItems()) {
                        ContentService contentService = new HttpContentService(serviceResponse.getId(), serviceResponse.getUrl(), accessToken);
                        contentService.setMessageLogger(messageLogger);
                        addContentService(contentService);
                    }
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            } catch (ServiceTimeoutException e) {
                e.printStackTrace();
            }
        }
        synchronized (contentServices) {
            if (contentServices.containsKey(serviceId)) {
                return contentServices.get(serviceId);
            }
        }
        return null;
    }

    /**
     * Get a ContentService client and configure it to use the specified url and access token
     * The service URL will be detected by calling Cloud Core service.
     *
     * @param cloudCoreUrl The endpoint URL of the Cloud Core service to use
     * @param accessToken  The OAuth access token to use for authorization
     * @return A client for ContentService
     */
    public static ContentService getContentService(String serviceId, String cloudCoreUrl, String accessToken) {
        return getContentService(serviceId, cloudCoreUrl, accessToken, null);
    }
}
