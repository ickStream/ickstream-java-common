/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import com.ickstream.protocol.ChunkedResponse;

import java.util.List;

public class PlaylistResponse extends ChunkedResponse {
    private Long lastChanged;
    private String playlistId;
    private String playlistName;
    private List<PlaylistItem> tracks_loop;

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

    public List<PlaylistItem> getTracks_loop() {
        return tracks_loop;
    }

    public void setTracks_loop(List<PlaylistItem> tracks_loop) {
        this.tracks_loop = tracks_loop;
    }
}
