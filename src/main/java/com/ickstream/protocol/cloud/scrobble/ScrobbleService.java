/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.scrobble;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.cloud.AbstractSyncService;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

public class ScrobbleService extends AbstractSyncService {

    public ScrobbleService(HttpClient client, String endpoint) {
        this(client, endpoint, null);
    }

    public ScrobbleService(HttpClient client, String endpoint, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    public Boolean playedTrack(PlayedItem playedItem) throws ServiceException, ServiceTimeoutException {
        return playedTrack(playedItem, (Integer) null);
    }

    public Boolean playedTrack(PlayedItem playedItem, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("playedTrack", playedItem, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void playedTrack(PlayedItem playedItem, MessageHandler<Boolean> messageHandler) {
        playedTrack(playedItem, messageHandler, (Integer) null);
    }

    public void playedTrack(PlayedItem playedItem, MessageHandler<Boolean> messageHandler, Integer timeout) {
        sendRequest("playedTrack", playedItem, Boolean.class, messageHandler, timeout);
    }
}
