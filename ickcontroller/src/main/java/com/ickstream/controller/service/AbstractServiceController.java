/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import com.ickstream.common.jsonrpc.JsonRpcResponse;
import com.ickstream.common.jsonrpc.JsonRpcResponseHandler;
import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.content.*;

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
    @Deprecated
    public void getProtocolDescription(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler) {
        contentService.getProtocolDescription(request, messageHandler);
    }

    @Override
    @Deprecated
    public void getProtocolDescription(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler, Integer timeout) {
        contentService.getProtocolDescription(request, messageHandler, timeout);
    }

    @Override
    public void getProtocolDescription2(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescription2Response> messageHandler) {
        contentService.getProtocolDescription2(request, messageHandler);
    }

    @Override
    public void getProtocolDescription2(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescription2Response> messageHandler, Integer timeout) {
        contentService.getProtocolDescription2(request, messageHandler, timeout);
    }

    @Override
    public void getPreferredMenus(GetPreferredMenusRequest request, MessageHandler<GetPreferredMenusResponse> messageHandler) {
        contentService.getPreferredMenus(request, messageHandler);
    }

    @Override
    public void getPreferredMenus(GetPreferredMenusRequest request, MessageHandler<GetPreferredMenusResponse> messageHandler, Integer timeout) {
        contentService.getPreferredMenus(request, messageHandler, timeout);
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

