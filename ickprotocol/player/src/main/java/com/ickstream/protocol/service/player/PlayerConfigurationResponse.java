/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlayerConfigurationResponse {
    String playerName;
    String playerModel;
    String hardwareId;
    String cloudCoreUrl;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(String playerModel) {
        this.playerModel = playerModel;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    public String getCloudCoreUrl() {
        return cloudCoreUrl;
    }

    public void setCloudCoreUrl(String cloudCoreUrl) {
        this.cloudCoreUrl = cloudCoreUrl;
    }
}
