/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.scrobble;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

public class ScrobbleService extends SyncJsonRpcClient implements Service {

    public ScrobbleService(HttpClient client, String endpoint) {
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

    public Boolean playedTrack(PlayedItem playedItem) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("playedTrack", playedItem, Boolean.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }
}
