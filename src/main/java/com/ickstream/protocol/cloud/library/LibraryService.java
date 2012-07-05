/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.library;

import com.ickstream.protocol.HttpJsonRpcClient;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class LibraryService implements Service {
    private JsonRpcClient jsonRpcClient;

    public LibraryService(HttpClient client, String endpoint) {
        jsonRpcClient = new HttpJsonRpcClient(client, endpoint);
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    @Override
    public ServiceInformation getServiceInformation() throws ServerException {
        return jsonRpcClient.callMethod("getServiceInformation", null, ServiceInformation.class);
    }

    public LibraryItem getTrack(String trackId) throws ServerException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        return jsonRpcClient.callMethod("getTrack", parameters, LibraryItem.class);
    }

    public boolean saveTrack(LibraryItem libraryItem) throws ServerException {
        return jsonRpcClient.callMethod("saveTrack", libraryItem, Boolean.class);
    }

    public Boolean removeTrack(String trackId) throws ServerException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        return jsonRpcClient.callMethod("removeTrack", parameters, Boolean.class);
    }
}
