/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.common.exception.UnauthorizedException;

public class AbstractService extends SyncJsonRpcClient implements Service {
    public AbstractService(MessageSender messageSender) {
        this(messageSender, (Integer) null);
    }

    public AbstractService(MessageSender messageSender, IdProvider idProvider) {
        this(messageSender, idProvider, null);
    }

    public AbstractService(MessageSender messageSender, Integer defaultTimeout) {
        this(messageSender, null, defaultTimeout);
    }

    public AbstractService(MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
        super(messageSender, idProvider, defaultTimeout);
    }

    protected ServiceException getServiceException(JsonRpcException e) throws ServiceException {
        if (e.getCode() == JsonRpcError.UNAUTHORIZED) {
            return new UnauthorizedException(e.getCode(), e.getMessage());
        } else {
            return new ServiceException(e.getCode(), e.getMessage() + "\n" + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    @Override
    public ProtocolVersionsResponse getProtocolVersions() throws ServiceException, ServiceTimeoutException {
        return getProtocolVersions((Integer) null);
    }

    @Override
    public ProtocolVersionsResponse getProtocolVersions(Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getProtocolVersions", null, ProtocolVersionsResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getProtocolVersions(MessageHandler<ProtocolVersionsResponse> messageHandler) {
        getProtocolVersions(messageHandler, (Integer) null);
    }

    public void getProtocolVersions(MessageHandler<ProtocolVersionsResponse> messageHandler, Integer timeout) {
        sendRequest("getProtocolVersions", null, ProtocolVersionsResponse.class, messageHandler, timeout);
    }

    @Override
    public ServiceInformation getServiceInformation() throws ServiceException, ServiceTimeoutException {
        return getServiceInformation((Integer) null);
    }

    @Override
    public ServiceInformation getServiceInformation(Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getServiceInformation", null, ServiceInformation.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getServiceInformation(MessageHandler<ServiceInformation> messageHandler) {
        getServiceInformation(messageHandler, (Integer) null);
    }

    public void getServiceInformation(MessageHandler<ServiceInformation> messageHandler, Integer timeout) {
        sendRequest("getServiceInformation", null, ServiceInformation.class, messageHandler, timeout);
    }
}
