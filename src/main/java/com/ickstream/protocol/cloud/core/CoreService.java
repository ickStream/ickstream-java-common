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

    public void getServiceInformation(MessageHandler<ServiceInformation> messageHandler) {
        sendRequest("getServiceInformation", null, ServiceInformation.class, messageHandler);
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

    public void findDevices(ChunkedRequest request, MessageHandler<FindDevicesResponse> messageHandler) {
        sendRequest("findDevices", request, FindDevicesResponse.class, messageHandler);
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

    public void getDevice(MessageHandler<DeviceResponse> messageHandler) {
        sendRequest("getDevice", null, DeviceResponse.class, messageHandler);
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

    public void getDevice(DeviceRequest request, MessageHandler<DeviceResponse> messageHandler) {
        sendRequest("getDevice", request, DeviceResponse.class, messageHandler);
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

    public void setDeviceAddress(SetDeviceAddressRequest request, MessageHandler<DeviceResponse> messageHandler) {
        sendRequest("setDeviceAddress", request, DeviceResponse.class, messageHandler);
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

    public void setDeviceName(SetDeviceNameRequest request, MessageHandler<DeviceResponse> messageHandler) {
        sendRequest("setDeviceName", request, DeviceResponse.class, messageHandler);
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

    public void addDevice(AddDeviceRequest request, MessageHandler<AddDeviceResponse> messageHandler) {
        sendRequest("addDevice", request, AddDeviceResponse.class, messageHandler);
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

    public void addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request, MessageHandler<AddDeviceResponse> messageHandler) {
        sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class, messageHandler);
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

    public void removeDevice(DeviceRequest request, MessageHandler<Boolean> messageHandler) {
        sendRequest("removeDevice", request, Boolean.class, messageHandler);
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

    public void getUser(MessageHandler<GetUserResponse> messageHandler) {
        sendRequest("getUser", null, GetUserResponse.class, messageHandler);
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

    public void findService(FindServicesRequest request, MessageHandler<FindDevicesResponse> messageHandler) {
        sendRequest("findServices", request, FindServicesResponse.class, messageHandler);
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

    public void findAllServices(FindServicesRequest request, MessageHandler<FindServicesResponse> messageHandler) {
        sendRequest("findAllServices", request, FindServicesResponse.class, messageHandler);
    }
}
