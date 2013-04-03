/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

import java.util.ArrayList;
import java.util.List;

public class PlaylistMoveTracksRequest {
    private String playlistId;
    private Integer playlistPos;
    private List<PlaylistItemReference> items = new ArrayList<PlaylistItemReference>();

    public PlaylistMoveTracksRequest(String playlistId) {
        this.playlistId = playlistId;
    }

    public PlaylistMoveTracksRequest(String playlistId, Integer playlistPos, List<PlaylistItemReference> items) {
        this.playlistId = playlistId;
        this.playlistPos = playlistPos;
        this.items = items;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }

    public List<PlaylistItemReference> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItemReference> items) {
        this.items = items;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}
