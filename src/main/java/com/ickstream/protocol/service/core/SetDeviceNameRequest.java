/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class SetDeviceNameRequest extends DeviceRequest {
    private String name;

    public SetDeviceNameRequest() {
    }

    public SetDeviceNameRequest(String name) {
        this.name = name;
    }

    public SetDeviceNameRequest(String deviceId, String name) {
        super(deviceId);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
