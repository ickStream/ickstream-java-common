/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service;

import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;

public interface Service {
    ServiceInformation getServiceInformation() throws ServiceException, ServiceTimeoutException;

    ServiceInformation getServiceInformation(Integer timeout) throws ServiceException, ServiceTimeoutException;

    void getServiceInformation(MessageHandler<ServiceInformation> messageHandler);

    void getServiceInformation(MessageHandler<ServiceInformation> messageHandler, Integer timeout);

    ProtocolVersionsResponse getProtocolVersions() throws ServiceException, ServiceTimeoutException;

    ProtocolVersionsResponse getProtocolVersions(Integer timeout) throws ServiceException, ServiceTimeoutException;

    void getProtocolVersions(MessageHandler<ProtocolVersionsResponse> messageHandler);

    void getProtocolVersions(MessageHandler<ProtocolVersionsResponse> messageHandler, Integer timeout);
}
