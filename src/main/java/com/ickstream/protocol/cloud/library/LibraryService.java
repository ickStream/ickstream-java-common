/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.library;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class LibraryService extends SyncJsonRpcClient implements Service {

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

    @Override
    public ServiceInformation getServiceInformation() throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getServiceInformation", null, ServiceInformation.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public LibraryItem getTrack(String trackId) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("trackId", trackId);
            return sendRequest("getTrack", parameters, LibraryItem.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public boolean saveTrack(LibraryItem libraryItem) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("saveTrack", libraryItem, Boolean.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }

    }

    public Boolean removeTrack(String trackId) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("trackId", trackId);
            return sendRequest("removeTrack", parameters, Boolean.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }
}
