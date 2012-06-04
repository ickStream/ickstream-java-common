/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistAddTracksRequest {
    private Integer playlistPos;
    private List<PlaylistItem> tracks_loop = new ArrayList<PlaylistItem>();

    public PlaylistAddTracksRequest() {
    }

    public PlaylistAddTracksRequest(List<PlaylistItem> tracks_loop) {
        this.tracks_loop = tracks_loop;
    }

    public PlaylistAddTracksRequest(Integer playlistPos, List<PlaylistItem> tracks_loop) {
        this.playlistPos = playlistPos;
        this.tracks_loop = tracks_loop;
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
