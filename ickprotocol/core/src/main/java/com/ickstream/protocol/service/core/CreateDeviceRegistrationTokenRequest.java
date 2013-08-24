/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class CreateDeviceRegistrationTokenRequest {
    private String id;
    private String name;
    private String applicationId;

    public CreateDeviceRegistrationTokenRequest() {
    }

    public CreateDeviceRegistrationTokenRequest(String id, String name, String applicationId) {
        this.id = id;
        this.name = name;
        this.applicationId = applicationId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }
}
