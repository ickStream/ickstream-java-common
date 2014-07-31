/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

/**
 * Message sender implementation that sends JSON-RPC message to the specified HTTP endpoint using HTTP POST messages
 */
public class HttpMessageSender implements MessageSender {
    private String endpoint;
    private String accessToken;
    private JsonRpcResponseHandler responseHandler;
    private JsonHelper jsonHelper = new JsonHelper();
    private HttpClient httpClient;
    private MessageLogger messageLogger;
    private Boolean asynchronous = false;

    /**
     * Creates a new message sender instance
     * The created instance will ignore responses unless {@link #setResponseHandler(JsonRpcResponseHandler)} is called to
     * specify a response handler.
     *
     * @param httpClient The {@link HttpClient} instance to use
     * @param endpoint   The HTTP endpoint url to send the message to
     */
    public HttpMessageSender(HttpClient httpClient, String endpoint) {
        this(httpClient, endpoint, false);
    }

    /**
     * Creates a new message sender instance
     *
     * @param httpClient      The {@link HttpClient} instance to use
     * @param endpoint        The HTTP endpoint url to send the message to
     * @param accessToken     The OAuth access token to use for authorization
     * @param responseHandler The response handler to use for handling responses
     */
    public HttpMessageSender(HttpClient httpClient, String endpoint, String accessToken, JsonRpcResponseHandler responseHandler) {
        this(httpClient, endpoint, false, accessToken, responseHandler);
    }

    /**
     * Creates a new message sender instance
     * The created instance will ignore responses unless {@link #setResponseHandler(JsonRpcResponseHandler)} is called to
     * specify a response handler.
     *
     * @param httpClient   The {@link HttpClient} instance to use
     * @param endpoint     The HTTP endpoint url to send the message to
     * @param asynchronous If true, the message will be sent in a new thread, this is typically useful if this message sender is used with {@link AsyncJsonRpcClient}
     */
    public HttpMessageSender(HttpClient httpClient, String endpoint, Boolean asynchronous) {
        this(httpClient, endpoint, asynchronous, null, null);
    }

    /**
     * Creates a new message sender instance
     *
     * @param httpClient      The {@link HttpClient} instance to use
     * @param endpoint        The HTTP endpoint url to send the message to
     * @param asynchronous    If true, the message will be sent in a new thread, this is typically useful if this message sender is used with {@link AsyncJsonRpcClient}
     * @param accessToken     The OAuth access token to use for authorization
     * @param responseHandler The response handler to use for handling responses
     */
    public HttpMessageSender(HttpClient httpClient, String endpoint, Boolean asynchronous, String accessToken, JsonRpcResponseHandler responseHandler) {
        this.httpClient = httpClient;
        this.endpoint = endpoint;
        this.accessToken = accessToken;
        this.responseHandler = responseHandler;
        this.asynchronous = asynchronous;
    }

    /**
     * Set the OAuth access token to use for authorization
     *
     * @param accessToken An OAuth access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Set the response handler to use when processing responses on message sent by this message sender
     *
     * @param responseHandler A response handler
     */
    public void setResponseHandler(JsonRpcResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    /**
     * Set the message logger implementation that should be used to log message sent and responses received using this
     * message sender
     *
     * @param messageLogger A message logger implementation
     */
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

    /**
     * Send the specified message using HTTP POST
     *
     * @param message The message to send
     */
    @Override
    public void sendMessage(String message) {
        final JsonRpcRequest request = jsonHelper.stringToObject(message, JsonRpcRequest.class);
        if (request == null) {
            reportInvalidJson(message);
            return;
        }
        final HttpClient httpClient = this.httpClient;
        final HttpPost httpRequest = new HttpPost(endpoint);
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
        httpRequest.setHeader("Authorization", "Bearer " + accessToken);
        if (messageLogger != null) {
            messageLogger.onOutgoingMessage(endpoint, message);
        }

        if (asynchronous) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    processMessage(httpClient, httpRequest, request);
                }
            }).start();
        } else {
            processMessage(httpClient, httpRequest, request);
        }
    }

    private void processMessage(final HttpClient httpClient, final HttpPost httpRequest, final JsonRpcRequest request) {
        try {
            final HttpResponse httpResponse = httpClient.execute(httpRequest);
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
