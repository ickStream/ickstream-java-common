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
