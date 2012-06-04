/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

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
