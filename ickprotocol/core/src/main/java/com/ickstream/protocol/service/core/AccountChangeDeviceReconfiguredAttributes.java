/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class AccountChangeDeviceReconfiguredAttributes extends AccountChangeDeviceAttributes {
    private String previousDeviceId;
    private String previousDeviceName;

    public AccountChangeDeviceReconfiguredAttributes() {
    }

    public AccountChangeDeviceReconfiguredAttributes(String previousDeviceId, String previousDeviceName, String deviceId, String deviceModel, String deviceName) {
        super(deviceId, deviceModel, deviceName);
        this.previousDeviceId = previousDeviceId;
        this.previousDeviceName = previousDeviceName;
    }

    public String getPreviousDeviceId() {
        return previousDeviceId;
    }

    public void setPreviousDeviceId(String previousDeviceId) {
        this.previousDeviceId = previousDeviceId;
    }

    public String getPreviousDeviceName() {
        return previousDeviceName;
    }

    public void setPreviousDeviceName(String previousDeviceName) {
        this.previousDeviceName = previousDeviceName;
    }
}
