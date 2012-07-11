/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.cloud.AbstractSyncService;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;

import java.util.HashMap;
import java.util.Map;

public abstract class ContentService extends AbstractSyncService implements Service {
    private String id;

    public ContentService(String id, MessageSender messageSender) {
        super(messageSender);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void setMessageLogger(MessageLogger messageLogger);

    public ProtocolDescriptionResponse getProtocolDescription() throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getProtocolDescription", null, ProtocolDescriptionResponse.class);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getProtocolDescription(MessageHandler<ProtocolDescriptionResponse> messageHandler) {
        sendRequest("getProtocolDescription", null, ProtocolDescriptionResponse.class, messageHandler);
    }

    public ContentResponse findTopLevelItems(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findTopLevelItems", request, ContentResponse.class);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findTopLevelItems(ChunkedRequest request, MessageHandler<ContentResponse> messageHandler) {
        sendRequest("findTopLevelItems", request, ContentResponse.class, messageHandler);
    }

    public ContentResponse findItems(ChunkedRequest request, String contextId, Map<String, Object> params) throws ServiceException, ServiceTimeoutException {
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
        try {
            return sendRequest("findItems", parameters, ContentResponse.class);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler) {
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
        sendRequest("findItems", parameters, ContentResponse.class, messageHandler);
    }
}
