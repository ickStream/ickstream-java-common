/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.Service;
import com.ickstream.protocol.cloud.AbstractSyncService;
import com.ickstream.protocol.cloud.ServiceException;
import com.ickstream.protocol.cloud.ServiceTimeoutException;
import org.apache.http.client.HttpClient;

public class CoreService extends AbstractSyncService implements Service {

    public CoreService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    public CoreService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    public CoreService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    public CoreService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    public FindDevicesResponse findDevices(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return findDevices(request, (Integer) null);
    }

    public FindDevicesResponse findDevices(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findDevices", request, FindDevicesResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findDevices(ChunkedRequest request, MessageHandler<FindDevicesResponse> messageHandler) {
        findDevices(request, messageHandler, null);
    }

    public void findDevices(ChunkedRequest request, MessageHandler<FindDevicesResponse> messageHandler, Integer timeout) {
        sendRequest("findDevices", request, FindDevicesResponse.class, messageHandler, timeout);
    }

    public DeviceResponse getDevice() throws ServiceException, ServiceTimeoutException {
        return getDevice((Integer) null);
    }

    public DeviceResponse getDevice(Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getDevice", null, DeviceResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getDevice(MessageHandler<DeviceResponse> messageHandler) {
        getDevice(messageHandler, null);
    }

    public void getDevice(MessageHandler<DeviceResponse> messageHandler, Integer timeout) {
        sendRequest("getDevice", null, DeviceResponse.class, messageHandler, timeout);
    }

    public DeviceResponse getDevice(DeviceRequest request) throws ServiceException, ServiceTimeoutException {
        return getDevice(request, (Integer) null);
    }

    public DeviceResponse getDevice(DeviceRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getDevice", request, DeviceResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getDevice(DeviceRequest request, MessageHandler<DeviceResponse> messageHandler) {
        getDevice(request, messageHandler, null);
    }

    public void getDevice(DeviceRequest request, MessageHandler<DeviceResponse> messageHandler, Integer timeout) {
        sendRequest("getDevice", request, DeviceResponse.class, messageHandler, timeout);
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request) throws ServiceException, ServiceTimeoutException {
        return setDeviceAddress(request, (Integer) null);
    }

    public DeviceResponse setDeviceAddress(SetDeviceAddressRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setDeviceAddress", request, DeviceResponse.class, (Integer) null);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void setDeviceAddress(SetDeviceAddressRequest request, MessageHandler<DeviceResponse> messageHandler) {
        setDeviceAddress(request, messageHandler, null);
    }

    public void setDeviceAddress(SetDeviceAddressRequest request, MessageHandler<DeviceResponse> messageHandler, Integer timeout) {
        sendRequest("setDeviceAddress", request, DeviceResponse.class, messageHandler, timeout);
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request) throws ServiceException, ServiceTimeoutException {
        return setDeviceName(request, (Integer) null);
    }

    public DeviceResponse setDeviceName(SetDeviceNameRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setDeviceName", request, DeviceResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void setDeviceName(SetDeviceNameRequest request, MessageHandler<DeviceResponse> messageHandler) {
        setDeviceName(request, messageHandler, (Integer) null);
    }

    public void setDeviceName(SetDeviceNameRequest request, MessageHandler<DeviceResponse> messageHandler, Integer timeout) {
        sendRequest("setDeviceName", request, DeviceResponse.class, messageHandler, timeout);
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request) throws ServiceException, ServiceTimeoutException {
        return addDevice(request, (Integer) null);
    }

    public AddDeviceResponse addDevice(AddDeviceRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("addDevice", request, AddDeviceResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void addDevice(AddDeviceRequest request, MessageHandler<AddDeviceResponse> messageHandler) {
        addDevice(request, messageHandler, (Integer) null);
    }

    public void addDevice(AddDeviceRequest request, MessageHandler<AddDeviceResponse> messageHandler, Integer timeout) {
        sendRequest("addDevice", request, AddDeviceResponse.class, messageHandler, timeout);
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) throws ServiceException, ServiceTimeoutException {
        return addDeviceWithHardwareId(request, (Integer) null);
    }

    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request, MessageHandler<AddDeviceResponse> messageHandler) {
        addDeviceWithHardwareId(request, messageHandler, (Integer) null);
    }

    public void addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request, MessageHandler<AddDeviceResponse> messageHandler, Integer timeout) {
        sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class, messageHandler, timeout);
    }

    public Boolean removeDevice(DeviceRequest request) throws ServiceException, ServiceTimeoutException {
        return removeDevice(request, (Integer) null);
    }

    public Boolean removeDevice(DeviceRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("removeDevice", request, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeDevice(DeviceRequest request, MessageHandler<Boolean> messageHandler) {
        removeDevice(request, messageHandler, (Integer) null);
    }

    public void removeDevice(DeviceRequest request, MessageHandler<Boolean> messageHandler, Integer timeout) {
        sendRequest("removeDevice", request, Boolean.class, messageHandler, timeout);
    }

    public GetUserResponse getUser() throws ServiceException, ServiceTimeoutException {
        return getUser((Integer) null);
    }

    public GetUserResponse getUser(Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getUser", null, GetUserResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getUser(MessageHandler<GetUserResponse> messageHandler) {
        getUser(messageHandler, (Integer) null);
    }

    public void getUser(MessageHandler<GetUserResponse> messageHandler, Integer timeout) {
        sendRequest("getUser", null, GetUserResponse.class, messageHandler, timeout);
    }

    public FindServicesResponse findServices(FindServicesRequest request) throws ServiceException, ServiceTimeoutException {
        return findServices(request, (Integer) null);
    }

    public FindServicesResponse findServices(FindServicesRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findServices", request, FindServicesResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findServices(FindServicesRequest request, MessageHandler<FindDevicesResponse> messageHandler) {
        findServices(request, messageHandler, (Integer) null);
    }

    public void findServices(FindServicesRequest request, MessageHandler<FindDevicesResponse> messageHandler, Integer timeout) {
        sendRequest("findServices", request, FindServicesResponse.class, messageHandler, timeout);
    }

    public FindServicesResponse findAllServices(FindServicesRequest request) throws ServiceException, ServiceTimeoutException {
        return findAllServices(request, (Integer) null);
    }

    public FindServicesResponse findAllServices(FindServicesRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findAllServices", request, FindServicesResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findAllServices(FindServicesRequest request, MessageHandler<FindServicesResponse> messageHandler) {
        findAllServices(request, messageHandler, (Integer) null);
    }

    public void findAllServices(FindServicesRequest request, MessageHandler<FindServicesResponse> messageHandler, Integer timeout) {
        sendRequest("findAllServices", request, FindServicesResponse.class, messageHandler, timeout);
    }
}
