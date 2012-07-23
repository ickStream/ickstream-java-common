/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.scrobble;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

public class ScrobbleService extends AbstractService {

    public ScrobbleService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    public ScrobbleService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    public ScrobbleService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    public ScrobbleService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
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
