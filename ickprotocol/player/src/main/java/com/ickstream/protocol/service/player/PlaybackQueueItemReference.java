/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;


public class PlaybackQueueItemReference {
    private String id;
    private Integer playbackQueuePos;

    public PlaybackQueueItemReference() {
    }

    public PlaybackQueueItemReference(String id) {
        this.id = id;
    }

    public PlaybackQueueItemReference(String id, Integer playbackQueuePos) {
        this.id = id;
        this.playbackQueuePos = playbackQueuePos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }
}
