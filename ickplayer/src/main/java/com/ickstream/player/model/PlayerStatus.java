/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.model;

import com.ickstream.protocol.service.player.PlaylistItem;
import org.codehaus.jackson.annotate.JsonIgnore;

public class PlayerStatus {
    private Long changedTimestamp = System.currentTimeMillis();
    private Boolean playing = Boolean.FALSE;
    private Double volumeLevel = 0.1;
    private Boolean muted = Boolean.FALSE;
    private Double seekPos;
    private Integer playlistPos;
    @JsonIgnore
    private Playlist playlist;

    @JsonIgnore
    private PlayerStatusStorage storage;

    public PlayerStatus() {
        this(new Playlist());
    }

    public PlayerStatus(Playlist playlist) {
        this.playlist = playlist;
    }

    public PlayerStatus(Playlist playlist, PlayerStatusStorage storage) {
        this(playlist);
        this.storage = storage;
    }

    public void updateTimestamp() {
        Long newTimestamp = System.currentTimeMillis();
        if (newTimestamp.equals(changedTimestamp)) {
            newTimestamp++;
        }
        changedTimestamp = newTimestamp;
        if (this.storage != null) {
            storage.store(this);
        }
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
        updateTimestamp();
    }

    public Double getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(Double volumeLevel) {
        this.volumeLevel = volumeLevel;
        updateTimestamp();
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
        updateTimestamp();
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
        if ((playlistPos == null && this.playlistPos != null) || (playlistPos != null && !playlistPos.equals(this.playlistPos))) {
            this.playlistPos = playlistPos;
            updateTimestamp();
        }
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        if (playlist != null) {
            this.playlist = playlist;
        } else {
            this.playlist = new Playlist();
        }
        updateTimestamp();
    }

    @JsonIgnore
    public PlaylistItem getCurrentPlaylistItem() {
        if (getPlaylistPos() != null) {
            if (getPlaylistPos() < getPlaylist().getItems().size()) {
                return getPlaylist().getItems().get(getPlaylistPos());
            }
        }
        return null;
    }

    public Long getChangedTimestamp() {
        return changedTimestamp;
    }

    public PlayerStatusStorage getStorage() {
        return storage;
    }

    public void setStorage(PlayerStatusStorage storage) {
        this.storage = storage;
    }
}
