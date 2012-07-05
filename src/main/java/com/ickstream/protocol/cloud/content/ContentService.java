/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;

import java.util.HashMap;
import java.util.Map;

public class ContentService implements Service {
    protected JsonRpcClient jsonRpcClient;
    protected String id;

    public ContentService(String id, JsonRpcClient jsonRpcClient) {
        this.id = id;
        this.jsonRpcClient = jsonRpcClient;
    }

    public ContentService(String id, JsonRpcClient jsonRpcClient, String accessToken) {
        this(id, jsonRpcClient);
        this.jsonRpcClient.setAccessToken(accessToken);
    }

    protected JsonRpcClient getJsonRpcClient() {
        return jsonRpcClient;
    }

    public String getId() {
        return id;
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    @Override
    public ServiceInformation getServiceInformation() throws ServerException {
        return jsonRpcClient.callMethod("getServiceInformation", null, ServiceInformation.class);
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
