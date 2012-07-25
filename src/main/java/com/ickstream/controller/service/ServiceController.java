/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.service;

import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.content.ContentResponse;
import com.ickstream.protocol.service.content.ProtocolDescriptionResponse;

import java.util.Map;

public interface ServiceController {
    String getId();

    String getName();

    String getServiceUrl();

    void getProtocolDescription(ChunkedRequest request, MessageHandler<ProtocolDescriptionResponse> messageHandler);

    void getProtocolDescription(ChunkedRequest request, MessageHandler<ProtocolDescriptionResponse> messageHandler, Integer timeout);

    void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler);

    void findItems(ChunkedRequest request, String contextId, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler, Integer timeout);
}
