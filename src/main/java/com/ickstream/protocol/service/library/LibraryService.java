/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.library;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.service.Service;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class LibraryService extends AbstractService implements Service {

    public LibraryService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    public LibraryService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    public LibraryService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    public LibraryService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

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
