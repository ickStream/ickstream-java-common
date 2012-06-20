/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

public class PlaylistChangedNotification {
    private String playlistId;
    private String playlistName;
    private Integer countAll;

    public PlaylistChangedNotification() {
    }

    public PlaylistChangedNotification(Integer count) {
        this.countAll = count;
    }

    public PlaylistChangedNotification(String playlistId, String playlistName, Integer countAll) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.countAll = countAll;
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
}
