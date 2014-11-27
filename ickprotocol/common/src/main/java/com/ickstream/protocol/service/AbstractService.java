/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.protocol.service;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.common.exception.UnauthorizedException;

/**
 * Abstract service clients that can make both asynchronous and synchronous calls to
 * ickStream services.
 * <p>
 * See the official API documentation for details regarding individual methods and parameters.
 * </p>
 */
public class AbstractService extends SyncJsonRpcClient implements Service {
    /**
     * Creates a new instance using the specified message sender
     *
     * @param messageSender The message sender to use
     */
    public AbstractService(MessageSender messageSender) {
        this(messageSender, (Integer) null);
    }

    /**
     * Creates a new instance using the specified message sender and identity provider
     *
     * @param messageSender The message sender to use
     * @param idProvider    The identity provider to use
     */
    public AbstractService(MessageSender messageSender, IdProvider idProvider) {
        this(messageSender, idProvider, null);
    }

    /**
     * Creates a new instance using the specified message sender with a default timeout
     *
     * @param messageSender  The message sender to use
     * @param defaultTimeout The default timeout to use unless explicitly specified in a method call
     */
    public AbstractService(MessageSender messageSender, Integer defaultTimeout) {
        this(messageSender, null, defaultTimeout);
    }

    /**
     * Creates a new instance using the specified message sender and identity provider using a default timeout
     *
     * @param messageSender  The message sender to use
     * @param idProvider     The identity provider to use
     * @param defaultTimeout The default timeout to use unless explicitly specified in a method call
     */
    public AbstractService(MessageSender messageSender, IdProvider idProvider, Integer defaultTimeout) {
        super(messageSender, idProvider, defaultTimeout);
    }

    /**
     * Translates a generic {@link JsonRpcException} to specific client side exception
     *
     * @param e The exception to translate
     * @return The specific client side exception
     */
    protected ServiceException getServiceException(JsonRpcException e) {
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
