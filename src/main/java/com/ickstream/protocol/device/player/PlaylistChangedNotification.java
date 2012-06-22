/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

public class PlaylistChangedNotification {
    private String playlistId;
    private String playlistName;
    private Integer countAll;
    private Long lastChanged;

    public PlaylistChangedNotification() {
    }

    public PlaylistChangedNotification(Integer count, Long lastChanged) {
        this.countAll = count;
        this.lastChanged = lastChanged;
    }

    public PlaylistChangedNotification(String playlistId, String playlistName, Integer countAll, Long lastChanged) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.countAll = countAll;
        this.lastChanged = lastChanged;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Integer getCountAll() {
        return countAll;
    }

    public void setCountAll(Integer countAll) {
        this.countAll = countAll;
    }

    public Long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Long lastChanged) {
        this.lastChanged = lastChanged;
    }
}
