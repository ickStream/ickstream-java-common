/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.corebackend;

public class UserServiceRequest {
    private String deviceId;
    private String identity;
    private String accessToken;
    private String accessTokenSecret;
    private String refreshToken;
    private String customData;

    public UserServiceRequest() {
    }

    public UserServiceRequest(String deviceId) {
        this.deviceId = deviceId;
    }

    public UserServiceRequest(String deviceId, UserServiceResponse response) {
        this(deviceId);
        this.identity = response.getIdentity();
        this.accessToken = response.getAccessToken();
        this.accessTokenSecret = response.getAccessTokenSecret();
        this.refreshToken = response.getRefreshToken();
        this.customData = response.getCustomData();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenSecret() {
        return accessTokenSecret;
    }

    public void setAccessTokenSecret(String accessTokenSecret) {
        this.accessTokenSecret = accessTokenSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getCustomData() {
        return customData;
    }

    public void setCustomData(String customData) {
        this.customData = customData;
    }
}
