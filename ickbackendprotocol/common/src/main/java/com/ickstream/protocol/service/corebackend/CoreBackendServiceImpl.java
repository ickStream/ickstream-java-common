/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.corebackend;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.backend.common.BackendRequestContext;
import com.ickstream.protocol.backend.common.InjectHelper;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.common.exception.UnauthorizedException;
import com.ickstream.protocol.service.ServiceInformation;
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
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
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
    public ApplicationResponse getApplicationById(@JsonRpcParam(name = "applicationId") String applicationId) {
        throw new RuntimeException(new UnauthorizedException(JsonRpcError.UNAUTHORIZED, "Not implemented"));
    }

    @Override
    public UserServiceResponse getUserService(String deviceId) {
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("deviceId", deviceId);
            return sendRequest("getUserService", request, UserServiceResponse.class, (Integer) null);
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
        try {
            Map<String, String> request = new HashMap<String, String>();
            request.put("deviceToken", deviceToken);
            DeviceResponse device = sendRequest("getDeviceByToken", request, DeviceResponse.class, (Integer) null);
            InjectHelper.instance(BackendRequestContext.class).setDevice(device);
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
            return sendRequest("getUserByToken", request, UserResponse.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }

    @Override
    public ServiceResponse getService() {
        try {
            return sendRequest("getService", null, ServiceResponse.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw new RuntimeException(getServiceException(e));
        } catch (JsonRpcTimeoutException e) {
            throw new RuntimeException(new ServiceTimeoutException(e));
        }
    }
}
