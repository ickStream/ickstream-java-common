/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class PlaybackQueueResponse extends ChunkedResponse {
    private Long lastChanged;
    private String playlistId;
    private String playlistName;
    private List<PlaybackQueueItem> items;

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

    public Long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Long lastChanged) {
        this.lastChanged = lastChanged;
    }

    public List<PlaybackQueueItem> getItems() {
        return items;
    }

    public void setItems(List<PlaybackQueueItem> items) {
        this.items = items;
    }
}
