/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.common.jsonrpc.HttpMessageSender;
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
        return sendRequest("getServiceInformation", null, ServiceInformation.class);
    }

    public FindDevicesResponse findDevices(ChunkedRequest request) throws ServerException {
        return sendRequest("findDevices", request, FindDevicesResponse.class);
    }

    public DeviceResponse getDevice() throws ServerException {
        return sendRequest("getDevice", null, DeviceResponse.class);
    }

    public DeviceResponse getDevice(DeviceRequest request) throws ServerException {
        return sendRequest("getDevice", request, DeviceResponse.class);
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request) throws ServerException {
        return sendRequest("setDeviceAddress", request, DeviceResponse.class);
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request) throws ServerException {
        return sendRequest("setDeviceName", request, DeviceResponse.class);
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request) throws ServerException {
        return sendRequest("addDevice", request, AddDeviceResponse.class);
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) throws ServerException {
        return sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class);
    }

    public Boolean removeDevice(DeviceRequest request) throws ServerException {
        return sendRequest("removeDevice", request, Boolean.class);
    }

    public GetUserResponse getUser() throws ServerException {
        return sendRequest("getUser", null, GetUserResponse.class);
    }

    public FindServicesResponse findServices(FindServicesRequest request) throws ServerException {
        return sendRequest("findServices", request, FindServicesResponse.class);
    }

    public FindServicesResponse findAllServices(FindServicesRequest request) throws ServerException {
        return sendRequest("findAllServices", request, FindServicesResponse.class);
    }
}
