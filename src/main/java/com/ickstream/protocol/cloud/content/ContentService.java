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
        this(id, messageSender, (Integer) null);
    }

    public ContentService(String id, MessageSender messageSender, IdProvider idProvider) {
        this(id, messageSender, idProvider, null);
    }

    public ContentService(String id, MessageSender messageSender, Integer defaultTimeout) {
        this(id, messageSender, null, defaultTimeout);
    }

    public ContentService(String id, MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
        super(messageSender, idProvider, defaultTimeout);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public abstract void setMessageLogger(MessageLogger messageLogger);

    public ProtocolDescriptionResponse getProtocolDescription(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return getProtocolDescription(request, (Integer) null);
    }

    public ProtocolDescriptionResponse getProtocolDescription(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getProtocolDescription", request, ProtocolDescriptionResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getProtocolDescription(ChunkedRequest request, MessageHandler<ProtocolDescriptionResponse> messageHandler) {
        getProtocolDescription(request, messageHandler, (Integer) null);
    }

    public void getProtocolDescription(ChunkedRequest request, MessageHandler<ProtocolDescriptionResponse> messageHandler, Integer timeout) {
        sendRequest("getProtocolDescription", request, ProtocolDescriptionResponse.class, messageHandler, timeout);
    }

    public ContentResponse findTopLevelItems(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return findTopLevelItems(request, (Integer) null);
    }

    public ContentResponse findTopLevelItems(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findTopLevelItems", request, ContentResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findTopLevelItems(ChunkedRequest request, MessageHandler<ContentResponse> messageHandler) {
        findTopLevelItems(request, messageHandler, (Integer) null);
    }

    public void findTopLevelItems(ChunkedRequest request, MessageHandler<ContentResponse> messageHandler, Integer timeout) {
        sendRequest("findTopLevelItems", request, ContentResponse.class, messageHandler, timeout);
    }

    public ContentResponse findItems(ChunkedRequest request, String contextId, Map<String, Object> params) throws ServiceException, ServiceTimeoutException {
        return findItems(request, contextId, params, (Integer) null);
    }

    public ContentResponse findItems(ChunkedRequest request, String contextId, Map<String, Object> params, Integer timeout) throws ServiceException, ServiceTimeoutException {
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
            return sendRequest("findItems", parameters, ContentResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler) {
        findItems(request, contextId, params, messageHandler, (Integer) null);
    }

    public void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler, Integer timeout) {
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
        sendRequest("findItems", parameters, ContentResponse.class, messageHandler, timeout);
    }
}
