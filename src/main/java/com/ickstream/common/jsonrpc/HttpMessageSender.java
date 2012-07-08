/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpMessageSender implements MessageSender {
    private String endpoint;
    private String accessToken;
    private JsonRpcResponseHandler responseHandler;
    private JsonHelper jsonHelper = new JsonHelper();
    private HttpClient httpClient;
    private MessageLogger messageLogger;

    public HttpMessageSender(HttpClient httpClient, String endpoint) {
        this.httpClient = httpClient;
        this.endpoint = endpoint;
    }

    public HttpMessageSender(HttpClient httpClient, String endpoint, String accessToken, JsonRpcResponseHandler responseHandler) {
        this.httpClient = httpClient;
        this.endpoint = endpoint;
        this.accessToken = accessToken;
        this.responseHandler = responseHandler;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setResponseHandler(JsonRpcResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    @Override
    public void sendMessage(String message) {
        try {
            HttpClient httpclient = httpClient;
            HttpPost httpRequest = new HttpPost(endpoint);
            httpRequest.setEntity(new StringEntity(message));
            httpRequest.setHeader("Authorization", "OAuth " + accessToken);
            if (messageLogger != null) {
                messageLogger.onOutgoingMessage(endpoint, message);
            }
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() < 400) {
                String responseString = EntityUtils.toString(httpResponse.getEntity());
                if (responseString != null && responseString.length() > 0) {
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(endpoint, responseString);
                    }
                    JsonRpcResponse response = jsonHelper.stringToObject(responseString, JsonRpcResponse.class);
                    if (response != null) {
                        responseHandler.onResponse(response);
                    }
                }
            } else {
                throw new RuntimeException(httpResponse.getStatusLine().getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            //TODO: Error handling
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            //TODO: Error handling
            e.printStackTrace();
        } catch (IOException e) {
            //TODO: Error handling
            e.printStackTrace();
        }
    }
}
