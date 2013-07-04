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

public class ScrobbleServiceFactory extends CoreServiceFactory {
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

    public static ScrobbleService getScrobbleService(String cloudCoreUrl, String accessToken) {
        return getScrobbleService(cloudCoreUrl, accessToken, null);
    }
}
