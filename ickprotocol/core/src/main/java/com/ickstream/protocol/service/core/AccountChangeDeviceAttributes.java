/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class AccountChangeDeviceAttributes {
    private String deviceId;
    private String deviceModel;
    private String deviceName;

    public AccountChangeDeviceAttributes() {
    }

    public AccountChangeDeviceAttributes(String deviceId, String deviceModel, String deviceName) {
        this.deviceId = deviceId;
        this.deviceModel = deviceModel;
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
