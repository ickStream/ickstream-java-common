/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import com.ickstream.common.jsonrpc.MessageHandler;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.content.*;

import java.util.Map;

public interface ServiceController {
    String getId();

    String getName();

    String getServiceUrl();

    void getPreferredMenus(GetPreferredMenusRequest request, MessageHandler<GetPreferredMenusResponse> messageHandler);

    void getPreferredMenus(GetPreferredMenusRequest request, MessageHandler<GetPreferredMenusResponse> messageHandler, Integer timeout);

    @Deprecated
    void getProtocolDescription(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler);

    @Deprecated
    void getProtocolDescription(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescriptionResponse> messageHandler, Integer timeout);

    void getProtocolDescription2(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescription2Response> messageHandler);

    void getProtocolDescription2(GetProtocolDescriptionRequest request, MessageHandler<GetProtocolDescription2Response> messageHandler, Integer timeout);

    void findItems(ChunkedRequest request, String contextId, String language, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler);

    void findItems(ChunkedRequest request, String contextId, String language, Map<String, Object> params, MessageHandler<ContentResponse> messageHandler, Integer timeout);
}
