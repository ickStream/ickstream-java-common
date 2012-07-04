/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud;

import com.ickstream.protocol.cloud.core.CoreService;
import com.ickstream.protocol.cloud.core.FindServicesRequest;
import com.ickstream.protocol.cloud.core.FindServicesResponse;
import com.ickstream.protocol.cloud.library.LibraryService;
import com.ickstream.protocol.cloud.scrobble.ScrobbleService;
import org.apache.http.client.HttpClient;

public class ServiceFactory {
    private static final String CORESERVICE_ENDPOINT = "http://ickstream.isaksson.info/ickstream-cloud-core/jsonrpc";

    public static CoreService getCoreService(HttpClient httpClient, String accessToken) {
        CoreService coreService = new CoreService(httpClient, CORESERVICE_ENDPOINT);
        coreService.setAccessToken(accessToken);
        return coreService;
    }

    public static ScrobbleService getScrobbleService(HttpClient httpClient, String accessToken) {
        try {
            FindServicesResponse response = getCoreService(httpClient, accessToken).findServices(new FindServicesRequest("scrobble"));
            if (response != null && response.getItems_loop().size() > 0) {
                ScrobbleService scrobbleService = new ScrobbleService(httpClient, response.getItems_loop().get(0).getUrl());
                scrobbleService.setAccessToken(accessToken);
                return scrobbleService;
            }
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static LibraryService getLibraryService(HttpClient httpClient, String accessToken) {
        try {
            FindServicesResponse response = getCoreService(httpClient, accessToken).findServices(new FindServicesRequest("librarymanagement"));
            if (response != null && response.getItems_loop().size() > 0) {
                LibraryService libraryService = new LibraryService(httpClient, response.getItems_loop().get(0).getUrl());
                libraryService.setAccessToken(accessToken);
                return libraryService;
            }
        } catch (ServerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
