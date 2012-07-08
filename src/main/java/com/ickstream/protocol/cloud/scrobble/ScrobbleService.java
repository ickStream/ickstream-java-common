/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.scrobble;

import com.ickstream.common.jsonrpc.HttpMessageSender;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.SyncJsonRpcClient;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

public class ScrobbleService extends SyncJsonRpcClient implements Service {

    public ScrobbleService(HttpClient client, String endpoint) {
        super(new HttpMessageSender(client, endpoint));
        ((HttpMessageSender)getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender)getMessageSender()).setAccessToken(accessToken);
    }

    @Override
    public ServiceInformation getServiceInformation() throws ServerException {
        return sendRequest("getServiceInformation", null, ServiceInformation.class);
    }

    public Boolean playedTrack(PlayedItem playedItem) throws ServerException {
        return sendRequest("playedTrack", playedItem, Boolean.class);
    }
}
