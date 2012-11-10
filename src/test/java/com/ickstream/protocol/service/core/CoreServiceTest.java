/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.core;

import com.ickstream.common.jsonrpc.InstanceIdProvider;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class CoreServiceTest {
    private static final String ENDPOINT = "http://example.org/ickstream-cloud-core/jsonrpc";

    private HttpClient createClient(final String endpoint, final String resultFile) {
        try {
            HttpClient client = Mockito.mock(HttpClient.class);
            HttpResponse response = Mockito.mock(HttpResponse.class);
            HttpEntity entity = new StringEntity(IOUtils.toString(getClass().getResource(resultFile)));
            Mockito.when(response.getEntity()).thenReturn(entity);
            StatusLine statusLine = Mockito.mock(StatusLine.class);
            Mockito.when(statusLine.getStatusCode()).thenReturn(200);
            Mockito.when(response.getStatusLine()).thenReturn(statusLine);
            Mockito.when(client.execute(Mockito.any(HttpPost.class))).thenReturn(response);
            return client;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private CoreService getCoreService(String responseData) {
        CoreService service = new CoreService(createClient(ENDPOINT, responseData), ENDPOINT, new InstanceIdProvider());
        service.setAccessToken("2E560913-F9BB-41A2-BAC4-A6EB272500EC");
        return service;
    }

    private PublicCoreService getPublicCoreService(String responseData) {
        PublicCoreService service = new PublicCoreService(createClient(ENDPOINT, responseData), ENDPOINT, new InstanceIdProvider());
        return service;
    }

    @Test
    public void testGetUser() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/GetUser.json");
        GetUserResponse response = service.getUser();
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getName(), "Some User");
        Assert.assertEquals(response.getId(), "2E560913-F9BB-41A2-BAC4-A6EB272500EC");
    }

    @Test
    public void testRemoveDevice() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/RemoveDevice.json");
        Boolean success = service.removeDevice(new DeviceRequest("F75C0B6A-06DB-44B4-8558-F8842C0EEB99"));
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    public void testSetDeviceName() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/SetDeviceName.json");
        SetDeviceNameRequest request = new SetDeviceNameRequest();
        request.setDeviceId("F75C0B6A-06DB-44B4-8558-F8842C0EEB99");
        request.setName("New Device Name");
        DeviceResponse response = service.setDeviceName(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getName());
        Assert.assertNotNull(response.getModel());
        Assert.assertNotNull(response.getAddress());
    }

    @Test
    public void testSetDeviceAddress() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/SetDeviceAddress.json");
        SetDeviceAddressRequest request = new SetDeviceAddressRequest();
        request.setDeviceId("F75C0B6A-06DB-44B4-8558-F8842C0EEB99");
        request.setAddress("127.0.0.3");
        DeviceResponse response = service.setDeviceAddress(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getName());
        Assert.assertNotNull(response.getModel());
        Assert.assertNotNull(response.getAddress());
    }

    @Test
    public void testGetDevice() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/GetDevice.json");
        DeviceRequest request = new DeviceRequest();
        request.setDeviceId("F75C0B6A-06DB-44B4-8558-F8842C0EEB99");
        DeviceResponse response = service.getDevice(request);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getId());
        Assert.assertNotNull(response.getName());
        Assert.assertNotNull(response.getModel());
        Assert.assertNotNull(response.getAddress());
    }

    @Test
    public void testAddDevice() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/AddDevice.json");
        AddDeviceRequest request = new AddDeviceRequest();
        request.setId("F75C0B6A-06DB-44B4-8558-F8842C0EEB99");
        request.setName("My Device");
        request.setAddress("127.0.0.2");
        request.setApplicationId("D586DB89-92B6-41B2-A579-AFA844158A99");
        request.setModel("Test Device");
        AddDeviceResponse response = service.addDevice(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertEquals(response.getName(), request.getName());
        Assert.assertEquals(response.getAddress(), request.getAddress());
        Assert.assertEquals(response.getModel(), request.getModel());
        Assert.assertNotNull(response.getAccessToken());
    }

    @Test
    public void testAddDeviceWithHardwareId() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/AddDeviceWithHardwareId.json");
        AddDeviceWithHardwareIdRequest request = new AddDeviceWithHardwareIdRequest();
        request.setId("F75C0B6A-06DB-44B4-8558-F8842C0EEB99");
        request.setName("My Device");
        request.setAddress("127.0.0.2");
        request.setApplicationId("D586DB89-92B6-41B2-A579-AFA844158A99");
        request.setHardwareId("A744682A-A9DB-419F-93FB-8E4F5CD54899");
        request.setModel("Test Device");
        AddDeviceResponse response = service.addDeviceWithHardwareId(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getId(), request.getId());
        Assert.assertEquals(response.getName(), request.getName());
        Assert.assertEquals(response.getAddress(), request.getAddress());
        Assert.assertEquals(response.getModel(), request.getModel());
        Assert.assertNotNull(response.getAccessToken());
    }

    @Test
    public void testFindDevices() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/FindDevices.json");
        FindDevicesResponse response = service.findDevices(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(2));
        Assert.assertEquals(response.getCountAll(), new Integer(2));
        Assert.assertNotNull(response.getItems());
        Assert.assertEquals(response.getItems().size(), 2);
        for (DeviceResponse deviceResponse : response.getItems()) {
            Assert.assertNotNull(deviceResponse.getId());
            Assert.assertNotNull(deviceResponse.getName());
            Assert.assertNotNull(deviceResponse.getModel());
            Assert.assertNotNull(deviceResponse.getAddress());
        }
    }

    @Test
    public void testFindServicesForType() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/FindServicesForType.json");
        FindServicesResponse response = service.findServices(new FindServicesRequest("library"));
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(1));
        Assert.assertEquals(response.getCountAll(), new Integer(1));
        Assert.assertNotNull(response.getItems());
        Assert.assertEquals(response.getItems().size(), 1);
        for (ServiceResponse serviceResponse : response.getItems()) {
            Assert.assertNotNull(serviceResponse.getId());
            Assert.assertNotNull(serviceResponse.getName());
            Assert.assertEquals(serviceResponse.getType(), "library");
            Assert.assertNotNull(serviceResponse.getUrl());
        }
    }

    @Test
    public void testFindServices() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/FindServices.json");
        FindServicesResponse response = service.findServices(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(2));
        Assert.assertEquals(response.getCountAll(), new Integer(2));
        Assert.assertNotNull(response.getItems());
        Assert.assertEquals(response.getItems().size(), 2);
        for (ServiceResponse serviceResponse : response.getItems()) {
            Assert.assertNotNull(serviceResponse.getId());
            Assert.assertNotNull(serviceResponse.getName());
            Assert.assertNotNull(serviceResponse.getType());
            Assert.assertNotNull(serviceResponse.getUrl());
        }
    }

    @Test
    public void testFindAllServices() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/FindAllServices.json");
        FindServicesResponse response = service.findAllServices(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(3));
        Assert.assertEquals(response.getCountAll(), new Integer(3));
        Assert.assertNotNull(response.getItems());
        Assert.assertEquals(response.getItems().size(), 3);
        for (ServiceResponse serviceResponse : response.getItems()) {
            Assert.assertNotNull(serviceResponse.getId());
            Assert.assertNotNull(serviceResponse.getName());
            Assert.assertNotNull(serviceResponse.getType());
            Assert.assertNotNull(serviceResponse.getUrl());
        }
    }

    @Test
    public void testFindAllServicesForType() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/FindAllServicesForType.json");
        FindServicesResponse response = service.findAllServices(new FindServicesRequest("content"));
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(2));
        Assert.assertEquals(response.getCountAll(), new Integer(2));
        Assert.assertNotNull(response.getItems());
        Assert.assertEquals(response.getItems().size(), 2);
        for (ServiceResponse serviceResponse : response.getItems()) {
            Assert.assertNotNull(serviceResponse.getId());
            Assert.assertNotNull(serviceResponse.getName());
            Assert.assertEquals(serviceResponse.getType(), "content");
            Assert.assertNotNull(serviceResponse.getUrl());
        }
    }

    @Test
    public void testRemoveService() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/RemoveService.json");
        Boolean success = service.removeService(new ServiceRequest("service1"));
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    public void testRemoveUserIdentity() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/RemoveUserIdentity.json");
        Boolean success = service.removeUserIdentity(new UserIdentityRequest("email", "test@example.org"));
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    public void testCreateUserCode() throws IOException, ServiceException, ServiceTimeoutException {
        CoreService service = getCoreService("/com/ickstream/protocol/service/core/CreateUserCode.json");
        String code = service.createUserCode();
        Assert.assertNotNull(code);
        Assert.assertEquals(code, "12AF25B7-1F76-46E8-8A8B-C33885C5D696");
    }

    @Test
    public void testFindAuthenticationProviders() throws IOException, ServiceException, ServiceTimeoutException {
        PublicCoreService service = getPublicCoreService("/com/ickstream/protocol/service/core/FindAuthenticationProviders.json");
        FindAuthenticationProviderResponse response = service.findAuthenticationProviders(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(2));
        Assert.assertEquals(response.getCountAll(), new Integer(2));
        Assert.assertNotNull(response.getItems());
        Assert.assertEquals(response.getItems().size(), 2);
        for (AuthenticationProviderResponse providerResponse : response.getItems()) {
            Assert.assertNotNull(providerResponse.getId());
            Assert.assertNotNull(providerResponse.getName());
            Assert.assertNotNull(providerResponse.getIcon());
            Assert.assertNotNull(providerResponse.getUrl());
        }
    }

}
