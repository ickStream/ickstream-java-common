/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

public class SetDeviceAddressRequest extends DeviceRequest {
    private String name;

    public String getAddress() {
        return name;
    }

    public void setAddress(String name) {
        this.name = name;
    }
}
