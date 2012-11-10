/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.Service;
import org.apache.http.client.HttpClient;

public class PublicCoreService extends AbstractService implements Service {
    public PublicCoreService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    public PublicCoreService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    public PublicCoreService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    public PublicCoreService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    public FindAuthenticationProviderResponse findAuthenticationProviders(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return findAuthenticationProviders(request, (Integer) null);
    }

    public FindAuthenticationProviderResponse findAuthenticationProviders(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findAuthenticationProviders", request, FindAuthenticationProviderResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findAuthenticationProviders(ChunkedRequest request, MessageHandler<FindAuthenticationProviderResponse> messageHandler) {
        findAuthenticationProviders(request, messageHandler, (Integer) null);
    }

    public void findAuthenticationProviders(ChunkedRequest request, MessageHandler<FindAuthenticationProviderResponse> messageHandler, Integer timeout) {
        sendRequest("findAuthenticationProviders", request, FindAuthenticationProviderResponse.class, messageHandler, timeout);
    }

}
