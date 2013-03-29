/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class RepeatModeRequest {
    private RepeatMode repeatMode;

    public RepeatModeRequest() {
    }

    public RepeatModeRequest(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }
}
