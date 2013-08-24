/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.Service;
import org.apache.http.client.HttpClient;

/**
 * Client class for accessing Cloud Core service
 * <p/>
 * See the official API documentation for details regarding individual methods and parameters.
 */
public class CoreService extends AbstractService implements Service {
    private String endpoint;

    /**
     * Creates a new instance using the specified HttpClient and endpoint URL
     *
     * @param client   The HttpClient instance to use when communicating
     * @param endpoint The endpoint URL to use when communicating
     */
    public CoreService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and identity provider
     *
     * @param client     The HttpClient instance to use when communicating
     * @param endpoint   The endpoint URL to use when communicating
     * @param idProvider The identity provider to use when generating JSON-RPC request identities
     */
    public CoreService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public CoreService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL, identity provider and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param idProvider     The identity provider to use when generating JSON-RPC request identities
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public CoreService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        this.endpoint = endpoint;
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    /**
     * Set message logging to use to log incoming and outgoing messages handled by this client
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    /**
     * Set access token to use for authorization
     *
     * @param accessToken OAuth access token to use for authorization
     */
    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    /**
     * Get the endpoint used by this object
     *
     * @return The endpoint used
     */
    public String getEndpoint() {
        return endpoint;
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

    @Deprecated
    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request) throws ServiceException, ServiceTimeoutException {
        return addDeviceWithHardwareId(request, (Integer) null);
    }

    @Deprecated
    public AddDeviceResponse addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("addDeviceWithHardwareId", request, AddDeviceResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    @Deprecated
    public void addDeviceWithHardwareId(AddDeviceWithHardwareIdRequest request, MessageHandler<AddDeviceResponse> messageHandler) {
        addDeviceWithHardwareId(request, messageHandler, (Integer) null);
    }

    @Deprecated
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

    public GetUserResponse setUserData(SetUserDataRequest request) throws ServiceException, ServiceTimeoutException {
        return setUserData(request, (Integer) null);
    }

    public GetUserResponse setUserData(SetUserDataRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setUserData", request, GetUserResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void setUserData(SetUserDataRequest request, MessageHandler<GetUserResponse> messageHandler) {
        setUserData(request, messageHandler, (Integer) null);
    }

    public void setUserData(SetUserDataRequest request, MessageHandler<GetUserResponse> messageHandler, Integer timeout) {
        sendRequest("setUserData", request, GetUserResponse.class, messageHandler, timeout);
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

    public void findServices(FindServicesRequest request, MessageHandler<FindServicesResponse> messageHandler) {
        findServices(request, messageHandler, (Integer) null);
    }

    public void findServices(FindServicesRequest request, MessageHandler<FindServicesResponse> messageHandler, Integer timeout) {
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

    public Boolean removeService(ServiceRequest request) throws ServiceException, ServiceTimeoutException {
        return removeService(request, (Integer) null);
    }

    public Boolean removeService(ServiceRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("removeService", request, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeService(ServiceRequest request, MessageHandler<Boolean> messageHandler) {
        removeService(request, messageHandler, (Integer) null);
    }

    public void removeService(ServiceRequest request, MessageHandler<Boolean> messageHandler, Integer timeout) {
        sendRequest("removeService", request, Boolean.class, messageHandler, timeout);
    }

    public Boolean removeUserIdentity(UserIdentityRequest request) throws ServiceException, ServiceTimeoutException {
        return removeUserIdentity(request, (Integer) null);
    }

    public Boolean removeUserIdentity(UserIdentityRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("removeUserIdentity", request, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeUserIdentity(UserIdentityRequest request, MessageHandler<Boolean> messageHandler) {
        removeUserIdentity(request, messageHandler, (Integer) null);
    }

    public void removeUserIdentity(UserIdentityRequest request, MessageHandler<Boolean> messageHandler, Integer timeout) {
        sendRequest("removeUserIdentity", request, Boolean.class, messageHandler, timeout);
    }

    public String createUserCode() throws ServiceException, ServiceTimeoutException {
        return createUserCode((Integer) null);
    }

    public String createUserCode(Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("createUserCode", null, String.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void createUserCode(MessageHandler<String> messageHandler) {
        createUserCode(messageHandler, (Integer) null);
    }

    public void createUserCode(MessageHandler<String> messageHandler, Integer timeout) {
        sendRequest("createUserCode", null, String.class, messageHandler, timeout);
    }

    public String createDeviceRegistrationToken(CreateDeviceRegistrationTokenRequest request) throws ServiceException, ServiceTimeoutException {
        return createDeviceRegistrationToken(request, (Integer) null);
    }

    public String createDeviceRegistrationToken(CreateDeviceRegistrationTokenRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("createDeviceRegistrationToken", request, String.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void createDeviceRegistrationToken(CreateDeviceRegistrationTokenRequest request, MessageHandler<String> messageHandler) {
        createDeviceRegistrationToken(request, messageHandler, (Integer) null);
    }

    public void createDeviceRegistrationToken(CreateDeviceRegistrationTokenRequest request, MessageHandler<String> messageHandler, Integer timeout) {
        sendRequest("createDeviceRegistrationToken", request, String.class, messageHandler, timeout);
    }

    public FindAccountChangesResponse findAccountChanges(ChunkedRequest request) throws ServiceException, ServiceTimeoutException {
        return findAccountChanges(request, (Integer) null);
    }

    public FindAccountChangesResponse findAccountChanges(ChunkedRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findAccountChanges", request, FindAccountChangesResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findAccountChanges(ChunkedRequest request, MessageHandler<FindAccountChangesResponse> messageHandler) {
        findAccountChanges(request, messageHandler, (Integer) null);
    }

    public void findAccountChanges(ChunkedRequest request, MessageHandler<FindAccountChangesResponse> messageHandler, Integer timeout) {
        sendRequest("findAccountChanges", request, FindAccountChangesResponse.class, messageHandler, timeout);
    }

}
