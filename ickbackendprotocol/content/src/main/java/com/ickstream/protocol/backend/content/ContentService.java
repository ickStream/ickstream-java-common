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

package com.ickstream.protocol.backend.content;

import com.ickstream.common.jsonrpc.JsonRpcError;
import com.ickstream.common.jsonrpc.JsonRpcErrors;
import com.ickstream.common.jsonrpc.JsonRpcParam;
import com.ickstream.common.jsonrpc.JsonRpcParamStructure;
import com.ickstream.protocol.backend.common.InvalidParameterException;
import com.ickstream.protocol.backend.common.PersonalizedService;
import com.ickstream.protocol.backend.common.UnauthorizedAccessException;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.common.data.StreamingReference;
import com.ickstream.protocol.service.content.*;

import java.util.Map;

public interface ContentService extends PersonalizedService {
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public ContentItem getItem(@JsonRpcParam(name = "contextId", optional = true) String contextId, @JsonRpcParam(name = "language", optional = true) String language, @JsonRpcParamStructure Map<String, String> parameters);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public StreamingReference getItemStreamingRef(@JsonRpcParam(name = "itemId") String itemId, @JsonRpcParamStructure GetItemStreamingRefRequest request);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })

    public ContentResponse findItems(@JsonRpcParam(name = "offset", optional = true) Integer offset, @JsonRpcParam(name = "count", optional = true) Integer count, @JsonRpcParam(name = "contextId", optional = true) String contextId, @JsonRpcParam(name = "language", optional = true) String language, @JsonRpcParamStructure Map<String, String> parameters);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public Boolean addItem(@JsonRpcParam(name = "contextId") String contextId, @JsonRpcParamStructure Map<String, String> parameters);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public Boolean removeItem(@JsonRpcParam(name = "contextId") String contextId, @JsonRpcParamStructure Map<String, String> parameters);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    @Deprecated
    public GetProtocolDescriptionResponse getProtocolDescription(@JsonRpcParam(name = "offset", optional = true) Integer offset, @JsonRpcParam(name = "count", optional = true) Integer count, @JsonRpcParam(name = "language", optional = true) String language);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public GetProtocolDescription2Response getProtocolDescription2(@JsonRpcParam(name = "offset", optional = true) Integer offset, @JsonRpcParam(name = "count", optional = true) Integer count, @JsonRpcParam(name = "language", optional = true) String language);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public GetPreferredMenusResponse getPreferredMenus(@JsonRpcParam(name = "offset", optional = true) Integer offset, @JsonRpcParam(name = "count", optional = true) Integer count, @JsonRpcParam(name = "language", optional = true) String language);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public GetManagementProtocolDescriptionResponse getManagementProtocolDescription(@JsonRpcParam(name = "offset", optional = true) Integer offset, @JsonRpcParam(name = "count", optional = true) Integer count);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public ContentResponse getNextDynamicPlaylistTracks(@JsonRpcParamStructure GetNextDynamicPlaylistTracksRequest request);
}
