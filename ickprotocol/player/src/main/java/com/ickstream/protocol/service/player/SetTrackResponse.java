/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class SetTrackResponse {
    private Integer playbackQueuePos;

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }
}
