/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.sun.jersey.api.client.*;
import org.apache.commons.io.IOUtils;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class CoreServiceTest {
    private static final String ENDPOINT = "http://example.org/ickstream-cloud-core/jsonrpc";

    private Client createClient(final String endpoint, final String resultFile) {
        Client client = new Client(new ClientHandler() {
            @Override
            public ClientResponse handle(ClientRequest cr) throws ClientHandlerException {
                Assert.assertEquals(cr.getURI().toString(), ENDPOINT);
                Assert.assertEquals(cr.getMethod(), "POST");
                ClientResponse clientResponse = Mockito.mock(ClientResponse.class);
                Mockito.when(clientResponse.getStatus()).thenReturn(ClientResponse.Status.OK.getStatusCode());
                Mockito.when(clientResponse.getClientResponseStatus()).thenReturn(ClientResponse.Status.OK);
                try {
                    Mockito.when(clientResponse.getEntity(String.class)).thenReturn(IOUtils.toString(getClass().getResourceAsStream(resultFile)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return clientResponse;
            }
        });
        return client;
    }


    private CoreService getCoreService(String responseData) {
        CoreService service = new CoreService(createClient(ENDPOINT, responseData), ENDPOINT);
        service.setAccessToken("2E560913-F9BB-41A2-BAC4-A6EB272500EC");
        return service;
    }

    @Test
    public void testGetUser() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/GetUser.json");
        GetUserResponse response = service.getUser();
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getName(), "Some User");
        Assert.assertEquals(response.getId(), "2E560913-F9BB-41A2-BAC4-A6EB272500EC");
    }

    @Test
    public void testRemoveDevice() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/RemoveDevice.json");
        Boolean success = service.removeDevice(new DeviceRequest("F75C0B6A-06DB-44B4-8558-F8842C0EEB99"));
        Assert.assertNotNull(success);
        Assert.assertTrue(success);
    }

    @Test
    public void testSetDeviceName() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/SetDeviceName.json");
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
    public void testSetDeviceAddress() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/SetDeviceAddress.json");
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
    public void testGetDevice() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/GetDevice.json");
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
    public void testAddDevice() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/AddDevice.json");
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
    public void testAddDeviceWithHardwareId() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/AddDeviceWithHardwareId.json");
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
    public void testFindDevices() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/FindDevices.json");
        FindDevicesResponse response = service.findDevices(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(2));
        Assert.assertEquals(response.getCountAll(), new Integer(2));
        Assert.assertNotNull(response.getItems_loop());
        Assert.assertEquals(response.getItems_loop().size(), 2);
        for (DeviceResponse deviceResponse : response.getItems_loop()) {
            Assert.assertNotNull(deviceResponse.getId());
            Assert.assertNotNull(deviceResponse.getName());
            Assert.assertNotNull(deviceResponse.getModel());
            Assert.assertNotNull(deviceResponse.getAddress());
        }
    }

    @Test
    public void testFindServices() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/FindServices.json");
        FindServicesResponse response = service.findServices(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(2));
        Assert.assertEquals(response.getCountAll(), new Integer(2));
        Assert.assertNotNull(response.getItems_loop());
        Assert.assertEquals(response.getItems_loop().size(), 2);
        for (ServiceResponse serviceResponse : response.getItems_loop()) {
            Assert.assertNotNull(serviceResponse.getId());
            Assert.assertNotNull(serviceResponse.getName());
            Assert.assertNotNull(serviceResponse.getUrl());
        }
    }

    @Test
    public void testFindAllServices() throws IOException {
        CoreService service = getCoreService("/com/ickstream/protocol/cloud/core/FindAllServices.json");
        FindServicesResponse response = service.findAllServices(null);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.getCount(), new Integer(3));
        Assert.assertEquals(response.getCountAll(), new Integer(3));
        Assert.assertNotNull(response.getItems_loop());
        Assert.assertEquals(response.getItems_loop().size(), 3);
        for (ServiceResponse serviceResponse : response.getItems_loop()) {
            Assert.assertNotNull(serviceResponse.getId());
            Assert.assertNotNull(serviceResponse.getName());
            Assert.assertNotNull(serviceResponse.getUrl());
        }
    }
}
