/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.common.jsonrpc.HttpMessageSender;
import com.ickstream.common.jsonrpc.JsonRpcException;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.SyncJsonRpcClient;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

public class CoreService extends SyncJsonRpcClient implements Service {

    public CoreService(HttpClient client, String endpoint) {
        super(new HttpMessageSender(client, endpoint));
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    @Override
    public ServiceInformation getServiceInformation() throws ServerException {
        try {
            return sendRequest("getServiceInformation", null, ServiceInformation.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public FindDevicesResponse findDevices(ChunkedRequest request) throws ServerException {
        try {
            return sendRequest("findDevices", request, FindDevicesResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public DeviceResponse getDevice() throws ServerException {
        try {
            return sendRequest("getDevice", null, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public DeviceResponse getDevice(DeviceRequest request) throws ServerException {
        try {
            return sendRequest("getDevice", request, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request) throws ServerException {
        try {
            return sendRequest("setDeviceAddress", request, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request) throws ServerException {
        try {
            return sendRequest("setDeviceName", request, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request) throws ServerException {
        try {
            return sendRequest("addDevice", request, AddDeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) throws ServerException {
        try {
            return sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public Boolean removeDevice(DeviceRequest request) throws ServerException {
        try {
            return sendRequest("removeDevice", request, Boolean.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public GetUserResponse getUser() throws ServerException {
        try {
            return sendRequest("getUser", null, GetUserResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public FindServicesResponse findServices(FindServicesRequest request) throws ServerException {
        try {
            return sendRequest("findServices", request, FindServicesResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }

    public FindServicesResponse findAllServices(FindServicesRequest request) throws ServerException {
        try {
            return sendRequest("findAllServices", request, FindServicesResponse.class);
        } catch (JsonRpcException e) {
            throw new ServerException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        }
    }
}
