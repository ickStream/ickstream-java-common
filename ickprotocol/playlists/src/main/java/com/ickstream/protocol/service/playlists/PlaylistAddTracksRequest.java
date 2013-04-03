/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAddTracksRequest {
    private String playlistId;
    private Integer playlistPos;
    private List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    public PlaylistAddTracksRequest(String playlistId) {
        this.playlistId = playlistId;
    }

    public PlaylistAddTracksRequest(String playlistId, List<PlaylistItem> tracks_loop) {
        this.playlistId = playlistId;
        this.items = tracks_loop;
    }

    public PlaylistAddTracksRequest(String playlistId, Integer playlistPos, List<PlaylistItem> items) {
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
}
