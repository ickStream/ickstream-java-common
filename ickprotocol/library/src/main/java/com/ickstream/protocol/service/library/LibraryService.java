/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.library;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.Service;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Client class for accessing Library service
 * <p/>
 * See the official API documentation for details regarding individual methods and parameters.
 */
public class LibraryService extends AbstractService implements Service {

    /**
     * Creates a new instance using the specified HttpClient and endpoint URL
     *
     * @param client   The HttpClient instance to use when communicating
     * @param endpoint The endpoint URL to use when communicating
     */
    public LibraryService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and identity provider
     *
     * @param client     The HttpClient instance to use when communicating
     * @param endpoint   The endpoint URL to use when communicating
     * @param idProvider The identity provider to use when generating JSON-RPC request identities
     */
    public LibraryService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public LibraryService(HttpClient client, String endpoint, Integer defaultTimeout) {
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
    public LibraryService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
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

    public LibraryItem getTrack(String trackId) throws ServiceException, ServiceTimeoutException {
        return getTrack(trackId, (Integer) null);
    }

    public LibraryItem getTrack(String trackId, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("trackId", trackId);
            return sendRequest("getTrack", parameters, LibraryItem.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getTrack(String trackId, MessageHandler<LibraryItem> messageHandler) {
        getTrack(trackId, messageHandler, (Integer) null);
    }

    public void getTrack(String trackId, MessageHandler<LibraryItem> messageHandler, Integer timeout) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        sendRequest("getTrack", parameters, LibraryItem.class, messageHandler, timeout);
    }

    public Boolean saveTrack(LibraryItem libraryItem) throws ServiceException, ServiceTimeoutException {
        return saveTrack(libraryItem, (Integer) null);
    }

    public Boolean saveTrack(LibraryItem libraryItem, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("saveTrack", libraryItem, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }

    }

    public void saveTrack(LibraryItem libraryItem, MessageHandler<Boolean> messageHandler) {
        saveTrack(libraryItem, messageHandler, (Integer) null);
    }

    public void saveTrack(LibraryItem libraryItem, MessageHandler<Boolean> messageHandler, Integer timeout) {
        sendRequest("saveTrack", libraryItem, Boolean.class, messageHandler, timeout);
    }

    public Boolean removeTrack(String trackId) throws ServiceException, ServiceTimeoutException {
        return removeTrack(trackId, (Integer) null);
    }

    public Boolean removeTrack(String trackId, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("trackId", trackId);
            return sendRequest("removeTrack", parameters, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeTrack(String trackId, MessageHandler<Boolean> messageHandler) {
        removeTrack(trackId, messageHandler, (Integer) null);
    }

    public void removeTrack(String trackId, MessageHandler<Boolean> messageHandler, Integer timeout) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        sendRequest("removeTrack", parameters, Boolean.class, messageHandler, timeout);
    }
}
