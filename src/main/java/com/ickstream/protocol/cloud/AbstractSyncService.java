/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;

public class AbstractSyncService extends SyncJsonRpcClient implements Service {
    public AbstractSyncService(MessageSender messageSender) {
        this(messageSender, (Integer) null);
    }

    public AbstractSyncService(MessageSender messageSender, IdProvider idProvider) {
        this(messageSender, idProvider, null);
    }

    public AbstractSyncService(MessageSender messageSender, Integer defaultTimeout) {
        this(messageSender, null, defaultTimeout);
    }

    public AbstractSyncService(MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
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
