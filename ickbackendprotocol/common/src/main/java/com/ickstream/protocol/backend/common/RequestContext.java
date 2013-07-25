/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

/**
 * A request context which contains information about the device or user making the current call.
 * This object can be retrieved using {@link InjectHelper#instance(Class)} and be used in the service to get information
 * about the device or user making the current call
 */
public class RequestContext {
    private String userId;
    private String deviceId;
    private String deviceAddress;
    private String contextUrl;

    public RequestContext() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getContextUrl() {
        return contextUrl;
    }

    public void setContextUrl(String contextUrl) {
        this.contextUrl = contextUrl;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
