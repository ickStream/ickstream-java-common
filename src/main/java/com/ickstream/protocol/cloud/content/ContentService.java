/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class ContentService {
    private JsonRpcClient jsonRpcClient;
    private String id;

    public String getId() {
        return id;
    }

    public ContentService(HttpClient client, String id, String endpoint) {
        this.id = id;
        jsonRpcClient = new JsonRpcClient(client, endpoint);
    }

    public ContentService(HttpClient client, String id, String endpoint, String accessToken) {
        this.id = id;
        jsonRpcClient = new JsonRpcClient(client, endpoint);
        jsonRpcClient.setAccessToken(accessToken);
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    public ProtocolDescriptionResponse getProtocolDescription() throws ServerException {
        return jsonRpcClient.callMethod("getProtocolDescription", null, ProtocolDescriptionResponse.class);
    }

    public ContentResponse findTopLevelItems(ChunkedRequest request) throws ServerException {
        return jsonRpcClient.callMethod("findTopLevelItems", request, ContentResponse.class);
    }

    public ContentResponse findItems(ChunkedRequest request, String contextId, Map<String, Object> params) throws ServerException {
        Map<String, Object> parameters = new HashMap<String, Object>();
        if (request != null) {
            if (request.getCount() != null) {
                parameters.put("count", request.getCount());
            }
            if (request.getOffset() != null) {
                parameters.put("offset", request.getOffset());
            }
        }
        if (contextId != null) {
            parameters.put("contextId", contextId);
        }
        parameters.putAll(params);
        return jsonRpcClient.callMethod("findItems", parameters, ContentResponse.class);

    }
}
