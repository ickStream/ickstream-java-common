/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSetTracksRequest {
    private String playlistId;
    private String playlistName;
    private Integer playlistPos;
    private List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    public PlaylistSetTracksRequest() {
    }

    public PlaylistSetTracksRequest(String playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public PlaylistSetTracksRequest(String playlistId, String playlistName, Integer playlistPos, List<PlaylistItem> items) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.playlistPos = playlistPos;
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
