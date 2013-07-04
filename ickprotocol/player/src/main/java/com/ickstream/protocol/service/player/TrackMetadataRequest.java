/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class TrackMetadataRequest {
    private Integer playbackQueuePos;
    private Boolean replace;
    private PlaybackQueueItem track;

    public TrackMetadataRequest() {
    }

    public TrackMetadataRequest(Integer playbackQueuePos, Boolean replace, PlaybackQueueItem track) {
        this.playbackQueuePos = playbackQueuePos;
        this.replace = replace;
        this.track = track;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }

    public Boolean getReplace() {
        return replace;
    }

    public void setReplace(Boolean replace) {
        this.replace = replace;
    }

    public PlaybackQueueItem getTrack() {
        return track;
    }

    public void setTrack(PlaybackQueueItem track) {
        this.track = track;
    }
}
