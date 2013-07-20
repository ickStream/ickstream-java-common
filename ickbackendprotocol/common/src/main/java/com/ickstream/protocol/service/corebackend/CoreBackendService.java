/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.corebackend;

import com.ickstream.common.jsonrpc.JsonRpcError;
import com.ickstream.common.jsonrpc.JsonRpcErrors;
import com.ickstream.common.jsonrpc.JsonRpcParam;
import com.ickstream.common.jsonrpc.JsonRpcParamStructure;
import com.ickstream.protocol.backend.common.CloudService;
import com.ickstream.protocol.backend.common.InvalidParameterException;
import com.ickstream.protocol.backend.common.UnauthorizedAccessException;

public interface CoreBackendService extends CloudService {
    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserServiceResponse getUserServiceByDevice(@JsonRpcParam(name = "deviceId") String deviceId);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserServiceResponse getUserServiceByUser(@JsonRpcParam(name = "userId") String userId);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserServiceResponse setUserService(@JsonRpcParamStructure UserServiceRequest request);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    ApplicationResponse getApplication();


    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    AuthenticationProviderResponse getAuthenticationProvider();

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
    UserResponse getUserById(@JsonRpcParam(name = "userId") String userId);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserResponse getUserByIdentity(@JsonRpcParam(name = "type") String type, @JsonRpcParam(name = "identity") String identity);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    UserResponse addIdentityToUser(@JsonRpcParam(name = "userCode") String userCode, @JsonRpcParam(name = "type") String type, @JsonRpcParam(name = "identity") String identity);


    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    String createAuthorizationCodeForIdentity(@JsonRpcParam(name = "type") String type, @JsonRpcParam(name = "identity") String identity, @JsonRpcParam(name = "accessToken") String accessToken, @JsonRpcParam(name = "accessTokenSecret") String accessTokenSecret, @JsonRpcParam(name = "redirectUri") String redirectUri);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    ServiceResponse getService();
}
