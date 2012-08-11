/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistMoveTracksRequest {
    private Integer playlistPos;
    private List<PlaylistItemReference> items = new ArrayList<PlaylistItemReference>();

    public PlaylistMoveTracksRequest() {
    }

    public PlaylistMoveTracksRequest(Integer playlistPos, List<PlaylistItemReference> items) {
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
}
