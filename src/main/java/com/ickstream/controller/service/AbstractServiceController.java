/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.service;

import com.ickstream.common.jsonrpc.JsonRpcResponse;
import com.ickstream.common.jsonrpc.JsonRpcResponseHandler;
import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.content.ContentResponse;
import com.ickstream.protocol.service.content.ContentService;
import com.ickstream.protocol.service.content.ProtocolDescriptionResponse;

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
    public void getProtocolDescription(ChunkedRequest request, MessageHandler<ProtocolDescriptionResponse> messageHandler) {
        contentService.getProtocolDescription(request, messageHandler);
    }

    @Override
    public void getProtocolDescription(ChunkedRequest request, MessageHandler<ProtocolDescriptionResponse> messageHandler, Integer timeout) {
        contentService.getProtocolDescription(request, messageHandler, timeout);
    }

    @Override
    public void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler) {
        contentService.findItems(request, contextId, params, messageHandler);
    }

    @Override
    public void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler, Integer timeout) {
        contentService.findItems(request, contextId, params, messageHandler, timeout);
    }

    @Override
    public boolean onResponse(JsonRpcResponse response) {
        return contentService.onResponse(response);
    }
}

