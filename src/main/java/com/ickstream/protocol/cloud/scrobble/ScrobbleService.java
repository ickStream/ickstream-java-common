/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.scrobble;

import com.ickstream.protocol.HttpJsonRpcClient;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

public class ScrobbleService implements Service {
    private JsonRpcClient jsonRpcClient;

    public ScrobbleService(HttpClient client, String endpoint) {
        jsonRpcClient = new HttpJsonRpcClient(client, endpoint);
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    @Override
    public ServiceInformation getServiceInformation() throws ServerException {
        return jsonRpcClient.callMethod("getServiceInformation", null, ServiceInformation.class);
    }

    public Boolean playedTrack(PlayedItem playedItem) throws ServerException {
        return jsonRpcClient.callMethod("playedTrack", playedItem, Boolean.class);
    }
}
