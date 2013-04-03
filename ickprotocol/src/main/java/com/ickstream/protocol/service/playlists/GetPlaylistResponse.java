/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;


import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class GetPlaylistResponse extends ChunkedResponse {
    private String playlistId;
    private String playlistName;
    private Long lastChanged;
    private List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
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

    public Long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Long lastChanged) {
        this.lastChanged = lastChanged;
    }
}
