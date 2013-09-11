/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.common.data.StreamingReference;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.AccountInformation;
import com.ickstream.protocol.service.PersonalizedService;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that implements client access to content services implementing the Content Access protocol
 * <p/>
 * See the official API documentation for details regarding individual methods and parameters.
 */
public abstract class ContentService extends AbstractService implements PersonalizedService {
    private String id;

    /**
     * Creates a new instance for the specified service identity using the specified message sender
     *
     * @param id            The identity of the service this client should communicate with
     * @param messageSender The message sender implementation which should be used when sending message to the service
     */
    public ContentService(String id, MessageSender messageSender) {
        this(id, messageSender, (Integer) null);
    }

    /**
     * Creates a new instance for the specified service identity using the specified message sender
     *
     * @param id            The identity of the service this client should communicate with
     * @param messageSender The message sender implementation which should be used when sending message to the service
     * @param idProvider    The identity provider to use to generate JSON-RPC request identities
     */
    public ContentService(String id, MessageSender messageSender, IdProvider idProvider) {
        this(id, messageSender, idProvider, null);
    }

    /**
     * Creates a new instance for the specified service identity using the specified message sender
     *
     * @param id             The identity of the service this client should communicate with
     * @param messageSender  The message sender implementation which should be used when sending message to the service
     * @param defaultTimeout The default timeout when not explicitly specified in a method call
     */
    public ContentService(String id, MessageSender messageSender, Integer defaultTimeout) {
        this(id, messageSender, null, defaultTimeout);
    }

    /**
     * Creates a new instance for the specified service identity using the specified message sender
     *
     * @param id             The identity of the service this client should communicate with
     * @param messageSender  The message sender implementation which should be used when sending message to the service
     * @param idProvider     The identity provider to use to generate JSON-RPC request identities
     * @param defaultTimeout The default timeout when not explicitly specified in a method call
     */
    public ContentService(String id, MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
        super(messageSender, idProvider, defaultTimeout);
        this.id = id;
    }

    /**
     * Get the identity of the service this client communicates with
     *
     * @return The identity of the service
     */
    public String getId() {
        return id;
    }

    /**
     * Set the message logger to use to log messages produced by this client
     *
     * @param messageLogger A message logger implementation
     */
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

    public StreamingReference getItemStreamingRef(GetItemStreamingRefRequest request) throws ServiceException, ServiceTimeoutException {
        return getItemStreamingRef(request, (Integer) null);
    }

    public StreamingReference getItemStreamingRef(GetItemStreamingRefRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getItemStreamingRef", request, StreamingReference.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getItemStreamingRef(GetItemStreamingRefRequest request, MessageHandler<StreamingReference> messageHandler) {
        getItemStreamingRef(request, messageHandler, (Integer) null);
    }

    public void getItemStreamingRef(GetItemStreamingRefRequest request, MessageHandler<StreamingReference> messageHandler, Integer timeout) {
        sendRequest("getItemStreamingRef", request, StreamingReference.class, messageHandler, timeout);
    }
}
