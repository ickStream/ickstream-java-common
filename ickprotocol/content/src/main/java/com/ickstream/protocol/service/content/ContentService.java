/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.AccountInformation;
import com.ickstream.protocol.service.PersonalizedService;

import java.util.HashMap;
import java.util.Map;

public abstract class ContentService extends AbstractService implements PersonalizedService {
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

    @Override
    public AccountInformation getAccountInformation() throws ServiceException, ServiceTimeoutException {
        return getAccountInformation((Integer) null);
    }

    @Override
    public AccountInformation getAccountInformation(Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getAccountInformation", null, AccountInformation.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getAccountInformation(MessageHandler<AccountInformation> messageHandler) {
        getAccountInformation(messageHandler, (Integer) null);
    }

    public void getAccountInformation(MessageHandler<AccountInformation> messageHandler, Integer timeout) {
        sendRequest("getAccountInformation", null, AccountInformation.class, messageHandler, timeout);
    }

    public GetProtocolDescriptionResponse getProtocolDescription(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return getProtocolDescription(request, (Integer) null);
    }

    public GetProtocolDescriptionResponse getProtocolDescription(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getProtocolDescription", request, GetProtocolDescriptionResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getProtocolDescription(ChunkedRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler) {
        getProtocolDescription(request, messageHandler, (Integer) null);
    }

    public void getProtocolDescription(ChunkedRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler, Integer timeout) {
        sendRequest("getProtocolDescription", request, GetProtocolDescriptionResponse.class, messageHandler, timeout);
    }

    public GetManagementProtocolDescriptionResponse getManagementProtocolDescription(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return getManagementProtocolDescription(request, (Integer) null);
    }

    public GetManagementProtocolDescriptionResponse getManagementProtocolDescription(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getManagementProtocolDescription", request, GetManagementProtocolDescriptionResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getManagementProtocolDescription(ChunkedRequest request, MessageHandler<GetManagementProtocolDescriptionResponse> messageHandler) {
        getManagementProtocolDescription(request, messageHandler, (Integer) null);
    }

    public void getManagementProtocolDescription(ChunkedRequest request, MessageHandler<GetManagementProtocolDescriptionResponse> messageHandler, Integer timeout) {
        sendRequest("getManagementProtocolDescription", request, GetManagementProtocolDescriptionResponse.class, messageHandler, timeout);
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
