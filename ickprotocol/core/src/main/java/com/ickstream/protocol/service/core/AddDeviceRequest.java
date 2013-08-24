/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class AddDeviceRequest {
    private String id;
    private String model;
    private String name;
    private String address;
    private String hardwareId;
    private String applicationId;

    public AddDeviceRequest() {
    }

    public AddDeviceRequest(String address, String hardwareId, String applicationId) {
        this.address = address;
        this.hardwareId = hardwareId;
        this.applicationId = applicationId;
    }

    @Deprecated
    public AddDeviceRequest(String model, String name, String address, String applicationId) {
        this.model = model;
        this.name = name;
        this.address = address;
        this.applicationId = applicationId;
    }

    @Deprecated
    public AddDeviceRequest(String id, String model, String name, String address, String applicationId) {
        this.id = id;
        this.model = model;
        this.name = name;
        this.address = address;
        this.applicationId = applicationId;
    }

    @Deprecated
    public String getId() {
        return id;
    }

    @Deprecated
    public void setId(String id) {
        this.id = id;
    }

    @Deprecated
    public String getModel() {
        return model;
    }

    @Deprecated
    public void setModel(String model) {
        this.model = model;
    }

    @Deprecated
    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(String name) {
        this.name = name;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
