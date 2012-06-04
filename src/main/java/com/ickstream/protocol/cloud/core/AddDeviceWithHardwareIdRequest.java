/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

public class AddDeviceWithHardwareIdRequest extends AddDeviceRequest {
    private String hardwareId;

    public AddDeviceWithHardwareIdRequest() {
    }

    public AddDeviceWithHardwareIdRequest(String model, String name, String address, String applicationId, String hardwareId) {
        super(model, name, address, applicationId);
        this.hardwareId = hardwareId;
    }

    public AddDeviceWithHardwareIdRequest(String id, String model, String name, String address, String applicationId, String hardwareId) {
        super(id, model, name, address, applicationId);
        this.hardwareId = hardwareId;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

}
