/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.model;

import com.ickstream.protocol.device.player.PlaylistItem;

public class PlayerStatus {
    private Boolean playing = Boolean.FALSE;
    private Double volumeLevel = 0.1;
    private Boolean muted = Boolean.FALSE;
    private Double seekPos;
    private Integer playlistPos;
    private Playlist playlist = new Playlist();

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
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

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public PlaylistItem getCurrentPlaylistItem() {
        if (getPlaylistPos() != null) {
            return getPlaylist().getItems().get(getPlaylistPos());
        }
        return null;
    }

}
