/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.JsonRpcClient;
import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.client.HttpClient;

public class CoreService {
    private JsonRpcClient jsonRpcClient;

    public CoreService(HttpClient client, String endpoint) {
        jsonRpcClient = new JsonRpcClient(client, endpoint);
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    public FindDevicesResponse findDevices(ChunkedRequest request) throws ServerException {
        return jsonRpcClient.callMethod("findDevices", request, FindDevicesResponse.class);
    }

    public DeviceResponse getDevice() throws ServerException {
        return jsonRpcClient.callMethod("getDevice", null, DeviceResponse.class);
    }

    public DeviceResponse getDevice(DeviceRequest request) throws ServerException {
        return jsonRpcClient.callMethod("getDevice", request, DeviceResponse.class);
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request) throws ServerException {
        return jsonRpcClient.callMethod("setDeviceAddress", request, DeviceResponse.class);
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request) throws ServerException {
        return jsonRpcClient.callMethod("setDeviceName", request, DeviceResponse.class);
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request) throws ServerException {
        return jsonRpcClient.callMethod("addDevice", request, AddDeviceResponse.class);
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) throws ServerException {
        return jsonRpcClient.callMethod("addDeviceWithHardwareId", request, AddDeviceResponse.class);
    }

    public Boolean removeDevice(DeviceRequest request) throws ServerException {
        return jsonRpcClient.callMethod("removeDevice", request, Boolean.class);
    }

    public GetUserResponse getUser() throws ServerException {
        return jsonRpcClient.callMethod("getUser", null, GetUserResponse.class);
    }

    public FindServicesResponse findServices(ChunkedRequest request) throws ServerException {
        return jsonRpcClient.callMethod("findServices", request, FindServicesResponse.class);
    }

    public FindServicesResponse findAllServices(ChunkedRequest request) throws ServerException {
        return jsonRpcClient.callMethod("findAllServices", request, FindServicesResponse.class);
    }
}
