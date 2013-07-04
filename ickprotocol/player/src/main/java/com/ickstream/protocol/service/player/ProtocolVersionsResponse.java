/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class ProtocolVersionsResponse {
    private String minVersion;
    private String maxVersion;

    public ProtocolVersionsResponse() {
    }

    public ProtocolVersionsResponse(String minVersion, String maxVersion) {
        this.minVersion = minVersion;
        this.maxVersion = maxVersion;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
    }
}
