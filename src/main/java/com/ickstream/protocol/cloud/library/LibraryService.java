/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.library;

import com.ickstream.common.jsonrpc.HttpMessageSender;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.SyncJsonRpcClient;
import com.ickstream.protocol.HttpJsonRpcClient;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;
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
    public ServiceInformation getServiceInformation() throws ServerException {
        return sendRequest("getServiceInformation", null, ServiceInformation.class);
    }

    public LibraryItem getTrack(String trackId) throws ServerException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        return sendRequest("getTrack", parameters, LibraryItem.class);
    }

    public boolean saveTrack(LibraryItem libraryItem) throws ServerException {
        return sendRequest("saveTrack", libraryItem, Boolean.class);
    }

    public Boolean removeTrack(String trackId) throws ServerException {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("trackId", trackId);
        return sendRequest("removeTrack", parameters, Boolean.class);
    }
}
