/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRemoveTracksRequest {
    private String playlistId;
    private List<PlaylistItemReference> items = new ArrayList<PlaylistItemReference>();

    public PlaylistRemoveTracksRequest(String playlistId) {
        this.playlistId = playlistId;
    }

    public PlaylistRemoveTracksRequest(String playlistId, List<PlaylistItemReference> items) {
        this.playlistId = playlistId;
        this.items = items;
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
