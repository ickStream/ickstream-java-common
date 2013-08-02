/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.Service;
import org.apache.http.client.HttpClient;

/**
 * Client class for accessing Cloud Core Authentiation service
 * <p/>
 * See the official API documentation for details regarding individual methods and parameters.
 */
public class PublicCoreService extends AbstractService implements Service {
    /**
     * Creates a new instance using the specified HttpClient and endpoint URL
     *
     * @param client   The HttpClient instance to use when communicating
     * @param endpoint The endpoint URL to use when communicating
     */
    public PublicCoreService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and identity provider
     *
     * @param client     The HttpClient instance to use when communicating
     * @param endpoint   The endpoint URL to use when communicating
     * @param idProvider The identity provider to use when generating JSON-RPC request identities
     */
    public PublicCoreService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public PublicCoreService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL, identity provider and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param idProvider     The identity provider to use when generating JSON-RPC request identities
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public PublicCoreService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    /**
     * Set message logging to use to log incoming and outgoing messages handled by this client
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    /**
     * Set access token to use for authorization
     *
     * @param accessToken OAuth access token to use for authorization
     */
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
