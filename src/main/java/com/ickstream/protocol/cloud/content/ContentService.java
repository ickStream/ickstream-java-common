/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;
import com.ickstream.common.jsonrpc.SyncJsonRpcClient;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;

import java.util.HashMap;
import java.util.Map;

public abstract class ContentService extends SyncJsonRpcClient implements Service {
    private String id;
    public ContentService(String id, MessageSender messageSender) {
        super(messageSender);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void setMessageLogger(MessageLogger messageLogger);

    @Override
    public ServiceInformation getServiceInformation() throws ServerException {
        return sendRequest("getServiceInformation", null, ServiceInformation.class);
    }

    public ProtocolDescriptionResponse getProtocolDescription() throws ServerException {
        return sendRequest("getProtocolDescription", null, ProtocolDescriptionResponse.class);
    }

    public ContentResponse findTopLevelItems(ChunkedRequest request) throws ServerException {
        return sendRequest("findTopLevelItems", request, ContentResponse.class);
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
        return sendRequest("findItems", parameters, ContentResponse.class);

    }
}
