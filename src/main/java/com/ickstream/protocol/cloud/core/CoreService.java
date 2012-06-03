/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.JsonRpcClient;
import org.apache.http.client.HttpClient;

public class CoreService {
    private JsonRpcClient jsonRpcClient;

    public CoreService(HttpClient client, String endpoint) {
        jsonRpcClient = new JsonRpcClient(client, endpoint);
    }

    public void setAccessToken(String accessToken) {
        jsonRpcClient.setAccessToken(accessToken);
    }

    public FindDevicesResponse findDevices(ChunkedRequest request) {
        return jsonRpcClient.callMethod("findDevices", request, FindDevicesResponse.class);
    }

    public DeviceResponse getDevice() {
        return jsonRpcClient.callMethod("getDevice", null, DeviceResponse.class);
    }

    public DeviceResponse getDevice(DeviceRequest request) {
        return jsonRpcClient.callMethod("getDevice", request, DeviceResponse.class);
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request) {
        return jsonRpcClient.callMethod("setDeviceAddress", request, DeviceResponse.class);
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request) {
        return jsonRpcClient.callMethod("setDeviceName", request, DeviceResponse.class);
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request) {
        return jsonRpcClient.callMethod("addDevice", request, AddDeviceResponse.class);
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) {
        return jsonRpcClient.callMethod("addDeviceWithHardwareId", request, AddDeviceResponse.class);
    }

    public Boolean removeDevice(DeviceRequest request) {
        return jsonRpcClient.callMethod("removeDevice", request, Boolean.class);
    }

    public GetUserResponse getUser() {
        return jsonRpcClient.callMethod("getUser", null, GetUserResponse.class);
    }

    public FindServicesResponse findServices(ChunkedRequest request) {
        return jsonRpcClient.callMethod("findServices", request, FindServicesResponse.class);
    }

    public FindServicesResponse findAllServices(ChunkedRequest request) {
        return jsonRpcClient.callMethod("findAllServices", request, FindServicesResponse.class);
    }
}
