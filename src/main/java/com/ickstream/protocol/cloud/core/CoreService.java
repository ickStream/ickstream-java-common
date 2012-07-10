/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.ServiceInformation;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;
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
    public ServiceInformation getServiceInformation() throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getServiceInformation", null, ServiceInformation.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public FindDevicesResponse findDevices(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findDevices", request, FindDevicesResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public DeviceResponse getDevice() throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getDevice", null, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public DeviceResponse getDevice(DeviceRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getDevice", request, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setDeviceAddress", request, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setDeviceName", request, DeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("addDevice", request, AddDeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public Boolean removeDevice(DeviceRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("removeDevice", request, Boolean.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public GetUserResponse getUser() throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getUser", null, GetUserResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public FindServicesResponse findServices(FindServicesRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findServices", request, FindServicesResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public FindServicesResponse findAllServices(FindServicesRequest request) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findAllServices", request, FindServicesResponse.class);
        } catch (JsonRpcException e) {
            throw new ServiceException(e.getCode(), e.getMessage() + (e.getData() != null ? "\n" + e.getData() : ""));
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }
}
