/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class VolumeResponse {
    private Double volumeLevel;
    private Boolean muted;

    public Double getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(Double volumeLevel) {
        this.volumeLevel = volumeLevel;
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
    }
}
