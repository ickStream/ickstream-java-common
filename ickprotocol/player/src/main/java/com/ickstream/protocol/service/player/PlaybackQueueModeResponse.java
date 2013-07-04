/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlaybackQueueModeResponse {
    private PlaybackQueueMode playbackQueueMode;

    public PlaybackQueueModeResponse() {
    }

    public PlaybackQueueModeResponse(PlaybackQueueMode playbackQueueMode) {
        this.playbackQueueMode = playbackQueueMode;
    }

    public PlaybackQueueMode getPlaybackQueueMode() {
        return playbackQueueMode;
    }

    public void setPlaybackQueueMode(PlaybackQueueMode playbackQueueMode) {
        this.playbackQueueMode = playbackQueueMode;
    }
}
