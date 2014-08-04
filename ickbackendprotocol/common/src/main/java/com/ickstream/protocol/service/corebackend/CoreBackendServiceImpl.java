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

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.backend.common.BackendRequestContext;
import com.ickstream.protocol.backend.common.CacheManager;
import com.ickstream.protocol.backend.common.InjectHelper;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.common.exception.UnauthorizedException;
import com.ickstream.protocol.service.ProtocolVersionsResponse;
import com.ickstream.protocol.service.ServiceInformation;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class CoreBackendServiceImpl extends SyncJsonRpcClient implements CoreBackendService {
    public CoreBackendServiceImpl(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    public CoreBackendServiceImpl(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    public CoreBackendServiceImpl(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    public CoreBackendServiceImpl(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    protected ServiceException getServiceException(JsonRpcException e) {
        if (e.getCode() == JsonRpcError.UNAUTHORIZED) {
            return new UnauthorizedException(e.getCode(), e.getMessage());
        } else {
            return new ServiceException(e.getCode(), e.getMessage() + "\n" + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    @Override
    public ProtocolVersionsResponse getProtocolVersions() {
        return new ProtocolVersionsResponse("1.0", "1.0");
    }

    @Override
    public AuthenticationProviderResponse getAuthenticationProvider() {
        BackendRequestContext requestContext = InjectHelper.instance(BackendRequestContext.class);
        if (requestContext.getAuthenticationProvider() != null) {
            return requestContext.getAuthenticationProvider();
        }
        try {
            AuthenticationProviderResponse provider = sendRequest("getAuthenticationProvider", null, AuthenticationProviderResponse.class, (Integer) null);
            requestContext.setAuthenticationProvider(provider);
            return provider;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public ApplicationResponse getApplication() {
        BackendRequestContext requestContext = InjectHelper.instance(BackendRequestContext.class);
        if (requestContext.getApplication() != null) {
            return requestContext.getApplication();
        }
        try {
            ApplicationResponse application = sendRequest("getApplication", null, ApplicationResponse.class, (Integer) null);
            requestContext.setApplication(application);
            return application;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public ApplicationResponse getApplicationById(@JsonRpcParam(name = "applicationId") String applicationId) {
        throw new RuntimeException(new UnauthorizedException(JsonRpcError.UNAUTHORIZED, "Not implemented"));
    }

    @Override
    public UserServiceResponse getUserServiceByDevice(String deviceId) {
        Cache cache = InjectHelper.instance(CacheManager.class).get(UserServiceResponse.class);
        if (cache != null) {
            Element cachedResult = cache.get(deviceId);
            if (cachedResult != null && !cachedResult.isExpired()) {
                UserServiceResponse userService = (UserServiceResponse) cachedResult.getValue();
                if (userService != null) {
                    return userService;
                }
            }
        }
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("deviceId", deviceId);
            UserServiceResponse response = sendRequest("getUserServiceByDevice", request, UserServiceResponse.class, (Integer) null);
            if (cache != null && response != null) cache.put(new Element(deviceId, response));
            return response;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public UserServiceResponse getUserServiceByUser(String userId) {
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("userId", userId);
            return sendRequest("getUserServiceByUser", request, UserServiceResponse.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public UserServiceResponse setUserService(UserServiceRequest request) {
        try {
            return sendRequest("setUserService", request, UserServiceResponse.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public ServiceInformation getServiceInformation() {
        try {
            return sendRequest("getServiceInformation", null, ServiceInformation.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public DeviceResponse getDeviceById(String deviceId) {
        BackendRequestContext requestContext = InjectHelper.instance(BackendRequestContext.class);
        if (requestContext.getDevice() != null && requestContext.getDevice().getId().equals(deviceId)) {
            return requestContext.getDevice();
        }
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("deviceId", deviceId);
            DeviceResponse device = sendRequest("getDeviceById", request, DeviceResponse.class, (Integer) null);
            requestContext.setDevice(device);
            return device;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public DeviceResponse getDeviceByToken(String deviceToken) {
        Cache cache = InjectHelper.instance(CacheManager.class).get(DeviceResponse.class);
        if (cache != null) {
            Element cachedResult = cache.get(deviceToken);
            if (cachedResult != null && !cachedResult.isExpired()) {
                DeviceResponse deviceResponse = (DeviceResponse) cachedResult.getValue();
                if (deviceResponse != null) {
                    InjectHelper.instance(BackendRequestContext.class).setDevice(deviceResponse);
                    return deviceResponse;
                }
            }
        }
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("deviceToken", deviceToken);
            DeviceResponse device = sendRequest("getDeviceByToken", request, DeviceResponse.class, (Integer) null);
            InjectHelper.instance(BackendRequestContext.class).setDevice(device);
            if (cache != null && device != null) cache.put(new Element(deviceToken, device));
            return device;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public UserResponse getUserByToken(String userToken) {
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("userToken", userToken);
            UserResponse userResponse = sendRequest("getUserByToken", request, UserResponse.class, (Integer) null);
            InjectHelper.instance(BackendRequestContext.class).setUser(userResponse);
            return userResponse;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public UserResponse getUserById(String userId) {
        BackendRequestContext requestContext = InjectHelper.instance(BackendRequestContext.class);
        if (requestContext.getUser() != null && requestContext.getUser().getId().equals(userId)) {
            return requestContext.getUser();
        }
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("userId", userId);
            UserResponse userResponse = sendRequest("getUserById", request, UserResponse.class, (Integer) null);
            requestContext.setUser(userResponse);
            return userResponse;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public UserResponse getUserByIdentity(String type, String identity) {
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("type", type);
            request.put("identity", identity);
            return sendRequest("getUserByIdentity", request, UserResponse.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public UserResponse addIdentityToUser(String userCode, String type, String identity) {
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("userCode", userCode);
            request.put("type", type);
            request.put("identity", identity);
            UserResponse userResponse = sendRequest("addIdentityToUser", request, UserResponse.class, (Integer) null);
            return userResponse;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public String createAuthorizationCodeForIdentity(String type, String identity, String serviceIdentity, String accessToken, String accessTokenSecret, String refreshToken, String customData, String redirectUri) {
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("type", type);
            request.put("identity", identity);
            request.put("serviceIdentity", serviceIdentity);
            request.put("accessToken", accessToken);
            request.put("accessTokenSecret", accessTokenSecret);
            request.put("refreshToken", refreshToken);
            request.put("redirectUri", redirectUri);
            return sendRequest("createAuthorizationCodeForIdentity", request, String.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public ServiceResponse getService() {
        BackendRequestContext requestContext = InjectHelper.instance(BackendRequestContext.class);
        if (requestContext.getService() != null) {
            return requestContext.getService();
        }
        try {
            ServiceResponse service = sendRequest("getService", null, ServiceResponse.class, (Integer) null);
            InjectHelper.instance(BackendRequestContext.class).setService(service);
            return service;
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }
}
