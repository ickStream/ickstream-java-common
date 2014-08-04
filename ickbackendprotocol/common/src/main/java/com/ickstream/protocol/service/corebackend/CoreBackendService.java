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
    String createAuthorizationCodeForIdentity(@JsonRpcParam(name = "type") String type, @JsonRpcParam(name = "identity") String identity, @JsonRpcParam(name = "serviceIdentity") String serviceIdentity, @JsonRpcParam(name = "accessToken") String accessToken, @JsonRpcParam(name = "accessTokenSecret") String accessTokenSecret, @JsonRpcParam(name = "refreshToken", optional = true) String refreshToken, @JsonRpcParam(name = "customData", optional = true) String customData, @JsonRpcParam(name = "redirectUri") String redirectUri);

    @JsonRpcErrors({
            @JsonRpcError(exception = InvalidParameterException.class, code = -32602, message = "Invalid method parameters"),
            @JsonRpcError(exception = UnauthorizedAccessException.class, code = -32000, message = "Unauthorized access")
    })
    ServiceResponse getService();
}
