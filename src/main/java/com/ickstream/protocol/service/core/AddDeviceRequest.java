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
    private String applicationId;

    public AddDeviceRequest() {
    }

    public AddDeviceRequest(String model, String name, String address, String applicationId) {
        this.model = model;
        this.name = name;
        this.address = address;
        this.applicationId = applicationId;
    }

    public AddDeviceRequest(String id, String model, String name, String address, String applicationId) {
        this.id = id;
        this.model = model;
        this.name = name;
        this.address = address;
        this.applicationId = applicationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
