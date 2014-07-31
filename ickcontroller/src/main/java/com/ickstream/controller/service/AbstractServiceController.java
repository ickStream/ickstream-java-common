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

