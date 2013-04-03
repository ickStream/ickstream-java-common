/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAddTracksRequest {
    private Integer playlistPos;
    private List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    public PlaylistAddTracksRequest() {
    }

    public PlaylistAddTracksRequest(List<PlaylistItem> tracks_loop) {
        this.items = tracks_loop;
    }

    public PlaylistAddTracksRequest(Integer playlistPos, List<PlaylistItem> items) {
        this.playlistPos = playlistPos;
        this.items = items;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
    }
}
