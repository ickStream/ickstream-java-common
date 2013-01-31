/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class SetDeviceAddressRequest extends DeviceRequest {
    private String address;

    public SetDeviceAddressRequest() {
    }

    public SetDeviceAddressRequest(String address) {
        this.address = address;
    }

    public SetDeviceAddressRequest(String deviceId, String address) {
        super(deviceId);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
