/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
