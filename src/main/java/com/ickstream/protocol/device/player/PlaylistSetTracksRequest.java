/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSetTracksRequest {
    private String playlistId;
    private String playlistName;
    private Integer playlistPos;
    private List<PlaylistItem> tracks_loop = new ArrayList<PlaylistItem>();

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

    public List<PlaylistItem> getTracks_loop() {
        return tracks_loop;
    }

    public void setTracks_loop(List<PlaylistItem> tracks_loop) {
        this.tracks_loop = tracks_loop;
    }
}
