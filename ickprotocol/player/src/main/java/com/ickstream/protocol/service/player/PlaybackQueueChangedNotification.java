/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class PlaybackQueueChangedNotification {
    private String playlistId;
    private String playlistName;
    private Integer countAll;
    private Long lastChanged;

    public PlaybackQueueChangedNotification() {
    }

    public PlaybackQueueChangedNotification(Integer count, Long lastChanged) {
        this.countAll = count;
        this.lastChanged = lastChanged;
    }

    public PlaybackQueueChangedNotification(String playlistId, String playlistName, Integer countAll, Long lastChanged) {
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
