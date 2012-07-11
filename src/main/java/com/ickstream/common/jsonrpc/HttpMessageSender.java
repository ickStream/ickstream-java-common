/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

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

    private void reportInvalidJson(String message) {
        if (responseHandler != null) {
            JsonRpcResponse response = new JsonRpcResponse("2.0", null);
            response.setError(new JsonRpcResponse.Error(JsonRpcError.INVALID_JSON, "Invalid JSON", message));
            String errorString = jsonHelper.objectToString(response);
            if (messageLogger != null) {
                messageLogger.onIncomingMessage(endpoint, errorString);
            }
            responseHandler.onResponse(response);
        }
    }

    @Override
    public void sendMessage(String message) {
        JsonRpcRequest request = jsonHelper.stringToObject(message, JsonRpcRequest.class);
        if (request == null) {
            reportInvalidJson(message);
            return;
        }
        HttpClient httpclient = httpClient;
        HttpPost httpRequest = new HttpPost(endpoint);
        try {
            Class.forName("org.apache.http.entity.ContentType");
            StringEntity stringEntity = new StringEntity(message, ContentType.create("application/json", Charset.forName("utf-8")));
            httpRequest.setEntity(stringEntity);
        } catch (ClassNotFoundException e) {
            try {
                StringEntity stringEntity = new StringEntity(message, "utf-8");
                httpRequest.setEntity(stringEntity);
            } catch (UnsupportedEncodingException e1) {
                reportInvalidJson(message);
                return;
            }
        }
        httpRequest.setHeader("Authorization", "OAuth " + accessToken);
        if (messageLogger != null) {
            messageLogger.onOutgoingMessage(endpoint, message);
        }
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpclient.execute(httpRequest);
            if (httpResponse.getStatusLine().getStatusCode() < 400) {
                String responseString = EntityUtils.toString(httpResponse.getEntity());
                if (responseString != null && responseString.length() > 0 && responseHandler != null) {
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(endpoint, responseString);
                    }
                    JsonRpcResponse response = jsonHelper.stringToObject(responseString, JsonRpcResponse.class);
                    if (response != null) {
                        responseHandler.onResponse(response);
                    }
                }
            } else if (httpResponse.getStatusLine().getStatusCode() == 401) {
                if (responseHandler != null) {
                    JsonRpcResponse response = new JsonRpcResponse(request.getJsonrpc(), request.getId());
                    response.setError(new JsonRpcResponse.Error(JsonRpcError.UNAUTHORIZED, "Unauthorized access", null));
                    String errorString = jsonHelper.objectToString(response);
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(endpoint, errorString);
                    }
                    responseHandler.onResponse(response);
                }
            } else {
                if (responseHandler != null) {
                    JsonRpcResponse response = new JsonRpcResponse(request.getJsonrpc(), request.getId());
                    response.setError(new JsonRpcResponse.Error(JsonRpcError.SERVICE_ERROR, httpResponse.getStatusLine().getReasonPhrase(), null));
                    String errorString = jsonHelper.objectToString(response);
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(endpoint, errorString);
                    }
                    responseHandler.onResponse(response);
                }
            }
        } catch (IOException e) {
            if (responseHandler != null) {
                JsonRpcResponse response = new JsonRpcResponse(request.getJsonrpc(), request.getId());
                response.setError(new JsonRpcResponse.Error(JsonRpcError.SERVICE_ERROR, e.getMessage(), null));
                String errorString = jsonHelper.objectToString(response);
                if (messageLogger != null) {
                    messageLogger.onIncomingMessage(endpoint, errorString);
                }
                responseHandler.onResponse(response);
            }
        }
    }
}
