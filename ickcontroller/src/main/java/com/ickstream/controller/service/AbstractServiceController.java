/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import com.ickstream.common.jsonrpc.JsonRpcResponse;
import com.ickstream.common.jsonrpc.JsonRpcResponseHandler;
import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.content.ContentResponse;
import com.ickstream.protocol.service.content.ContentService;
import com.ickstream.protocol.service.content.GetProtocolDescriptionRequest;
import com.ickstream.protocol.service.content.GetProtocolDescriptionResponse;

import java.util.Map;

public class AbstractServiceController implements ServiceController, JsonRpcResponseHandler {
    protected Service service;
    protected ContentService contentService;

    public AbstractServiceController(ContentService contentService, Service service) {
        this.contentService = contentService;
        this.service = service;
    }

    @Override
    public String getName() {
        return service.getName();
    }

    @Override
    public String getId() {
        return service.getId();
    }

    @Override
    public String getServiceUrl() {
        return service.getServiceUrl();
    }

    @Override
    public void getProtocolDescription(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler) {
        contentService.getProtocolDescription(request, messageHandler);
    }

    @Override
    public void getProtocolDescription(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler, Integer timeout) {
        contentService.getProtocolDescription(request, messageHandler, timeout);
    }

    @Override
    public void findItems(ChunkedRequest request, String contextId, String language, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler) {
        contentService.findItems(request, contextId, language, params, messageHandler);
    }

    @Override
    public void findItems(ChunkedRequest request, String contextId, String language, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler, Integer timeout) {
        contentService.findItems(request, contextId, language, params, messageHandler, timeout);
    }

    @Override
    public boolean onResponse(JsonRpcResponse response) {
        return contentService.onResponse(response);
    }
}

