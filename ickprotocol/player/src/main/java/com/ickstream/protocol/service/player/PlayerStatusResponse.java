/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlayerStatusResponse {
    private Long lastChanged;
    private Boolean playing;
    private Double seekPos;
    private Integer playbackQueuePos;
    private Double volumeLevel;
    private Boolean muted;
    private PlaybackQueueItem track;
    private PlaybackQueueMode playbackQueueMode;

    public Long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Long lastChanged) {
        this.lastChanged = lastChanged;
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
    }

    public Double getSeekPos() {
        return seekPos;
    }

    public void setSeekPos(Double seekPos) {
        this.seekPos = seekPos;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }

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

    public PlaybackQueueItem getTrack() {
        return track;
    }

    public void setTrack(PlaybackQueueItem track) {
        this.track = track;
    }

    public PlaybackQueueMode getPlaybackQueueMode() {
        return playbackQueueMode;
    }

    public void setPlaybackQueueMode(PlaybackQueueMode playbackQueueMode) {
        this.playbackQueueMode = playbackQueueMode;
    }
}
