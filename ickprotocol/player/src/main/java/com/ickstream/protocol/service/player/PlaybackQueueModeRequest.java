/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlaybackQueueModeRequest {
    private PlaybackQueueMode playbackQueueMode;

    public PlaybackQueueModeRequest() {
    }

    public PlaybackQueueModeRequest(PlaybackQueueMode playbackQueueMode) {
        this.playbackQueueMode = playbackQueueMode;
    }

    public PlaybackQueueMode getPlaybackQueueMode() {
        return playbackQueueMode;
    }

    public void setPlaybackQueueMode(PlaybackQueueMode playbackQueueMode) {
        this.playbackQueueMode = playbackQueueMode;
    }
}
