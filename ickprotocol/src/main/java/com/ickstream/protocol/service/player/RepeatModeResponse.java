/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class RepeatModeResponse {
    private RepeatMode repeatMode;

    public RepeatModeResponse() {
    }

    public RepeatModeResponse(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }
}
