/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlayerConfigurationRequest {
    String playerName;
    String accessToken;
    String cloudCoreUrl;
    String deviceRegistrationToken;

    public PlayerConfigurationRequest() {
    }

    @Deprecated
    public PlayerConfigurationRequest(String playerName, String accessToken) {
        this.playerName = playerName;
        this.accessToken = accessToken;
    }

    @Deprecated
    public PlayerConfigurationRequest(String playerName, String accessToken, String cloudCoreUrl) {
        this.playerName = playerName;
        this.accessToken = accessToken;
        this.cloudCoreUrl = cloudCoreUrl;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    @Deprecated
    public String getAccessToken() {
        return accessToken;
    }

    @Deprecated
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getDeviceRegistrationToken() {
        return deviceRegistrationToken;
    }

    public void setDeviceRegistrationToken(String deviceRegistrationToken) {
        this.deviceRegistrationToken = deviceRegistrationToken;
    }

    public String getCloudCoreUrl() {
        return cloudCoreUrl;
    }

    public void setCloudCoreUrl(String cloudCoreUrl) {
        this.cloudCoreUrl = cloudCoreUrl;
    }
}
