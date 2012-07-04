/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.scrobble;

import com.ickstream.protocol.HttpJsonRpcClient;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

public class ScrobbleService {
    private JsonRpcClient jsonRpcClient;

    public ScrobbleService(HttpClient client, String endpoint) {
        jsonRpcClient = new HttpJsonRpcClient(client, endpoint);
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    public Boolean playedTrack(PlayedItem playedItem) throws ServerException {
        return jsonRpcClient.callMethod("playedTrack", playedItem, Boolean.class);
    }
}
