/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlaybackQueueModificationResponse {
    private Boolean result;
    private Integer playbackQueuePos;

    public PlaybackQueueModificationResponse() {
    }

    public PlaybackQueueModificationResponse(Boolean result, Integer playbackQueuePos) {
        this.result = result;
        this.playbackQueuePos = playbackQueuePos;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }
}
