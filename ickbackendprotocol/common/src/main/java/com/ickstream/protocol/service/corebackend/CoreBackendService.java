/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.corebackend;

import com.ickstream.common.jsonrpc.JsonRpcError;
import com.ickstream.common.jsonrpc.JsonRpcErrors;
import com.ickstream.common.jsonrpc.JsonRpcParam;
import com.ickstream.protocol.backend.common.CloudService;
import com.ickstream.protocol.backend.common.InvalidParameterException;
import com.ickstream.protocol.backend.common.UnauthorizedAccessException;

public interface CoreBackendService extends CloudService {
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserServiceResponse getUserService(@JsonRpcParam(name = "deviceId") String deviceId);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    ApplicationResponse getApplicationById(@JsonRpcParam(name = "applicationId") String applicationId);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    DeviceResponse getDeviceById(@JsonRpcParam(name = "deviceId") String deviceId);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    DeviceResponse getDeviceByToken(@JsonRpcParam(name = "deviceToken") String deviceToken);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserResponse getUserByToken(@JsonRpcParam(name = "userToken") String userToken);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    ServiceResponse getService();
}
