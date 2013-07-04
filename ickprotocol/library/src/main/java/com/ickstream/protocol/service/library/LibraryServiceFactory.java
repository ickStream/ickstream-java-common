/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.library;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.core.CoreServiceFactory;
import com.ickstream.protocol.service.core.FindServicesRequest;
import com.ickstream.protocol.service.core.FindServicesResponse;
import org.apache.http.impl.client.DefaultHttpClient;

public class LibraryServiceFactory extends CoreServiceFactory {
    public static LibraryService getLibraryService(String accessToken, MessageLogger messageLogger) {
        return getLibraryService(null, accessToken, messageLogger);
    }

    public static LibraryService getLibraryService(String cloudCoreUrl, String accessToken, MessageLogger messageLogger) {
        try {
            FindServicesResponse response = getCoreService(cloudCoreUrl, accessToken, messageLogger).findServices(new FindServicesRequest("librarymanagement"));
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
        return getLibraryService(null, accessToken);
    }

    public static LibraryService getLibraryService(String cloudCoreUrl, String accessToken) {
        return getLibraryService(cloudCoreUrl, accessToken, null);
    }
}
