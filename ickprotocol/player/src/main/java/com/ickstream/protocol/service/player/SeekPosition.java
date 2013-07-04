/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class SeekPosition {
    private Integer playbackQueuePos;
    private Double seekPos;

    public SeekPosition() {
    }

    public SeekPosition(Integer playbackQueuePos, Double seekPos) {
        this.playbackQueuePos = playbackQueuePos;
        this.seekPos = seekPos;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }

    public Double getSeekPos() {
        return seekPos;
    }

    public void setSeekPos(Double seekPos) {
        this.seekPos = seekPos;
    }
}
