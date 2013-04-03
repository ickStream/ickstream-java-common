/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlayerStatusResponse {
    private Long lastChanged;
    private Boolean playing;
    private Double seekPos;
    private Integer playlistPos;
    private Double volumeLevel;
    private Boolean muted;
    private PlaylistItem track;
    private RepeatMode repeatMode;

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

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
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

    public PlaylistItem getTrack() {
        return track;
    }

    public void setTrack(PlaylistItem track) {
        this.track = track;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }
}
