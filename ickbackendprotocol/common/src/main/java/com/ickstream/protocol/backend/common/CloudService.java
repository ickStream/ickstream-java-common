/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.common.jsonrpc.JsonRpcError;
import com.ickstream.common.jsonrpc.JsonRpcErrors;
import com.ickstream.protocol.service.ProtocolVersionsResponse;
import com.ickstream.protocol.service.ServiceInformation;

/**
 * This interface contains the methods which all services must implement independent of which type of services it is.
 */
public interface CloudService {
    /**
     * Get information about the service
     *
     * @return Information about this service
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public ServiceInformation getServiceInformation();

    /**
     * Get information about the supported protocol versions
     *
     * @return Information about protocol version supported by this service
     */
    @JsonRpcErrors({
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    public ProtocolVersionsResponse getProtocolVersions();
}
