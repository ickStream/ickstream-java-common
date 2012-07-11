/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.library;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.cloud.AbstractSyncService;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class LibraryService extends AbstractSyncService implements Service {

    public LibraryService(HttpClient client, String endpoint) {
        super(new HttpMessageSender(client, endpoint));
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    public LibraryItem getTrack(String trackId) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("trackId", trackId);
            return sendRequest("getTrack", parameters, LibraryItem.class);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getTrack(String trackId, MessageHandler<LibraryItem> messageHandler) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        sendRequest("getTrack", parameters, LibraryItem.class, messageHandler);
    }

    public Boolean saveTrack(LibraryItem libraryItem) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("saveTrack", libraryItem, Boolean.class);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }

    }

    public void saveTrack(LibraryItem libraryItem, MessageHandler<Boolean> messageHandler) {
        sendRequest("saveTrack", libraryItem, Boolean.class, messageHandler);
    }

    public Boolean removeTrack(String trackId) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("trackId", trackId);
            return sendRequest("removeTrack", parameters, Boolean.class);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeTrack(String trackId, MessageHandler<Boolean> messageHandler) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        sendRequest("removeTrack", parameters, Boolean.class, messageHandler);
    }
}
