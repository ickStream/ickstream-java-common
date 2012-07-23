/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistMoveTracksRequest {
    private Integer playlistPos;
    private List<PlaylistItemReference> tracks_loop = new ArrayList<PlaylistItemReference>();

    public PlaylistMoveTracksRequest() {
    }

    public PlaylistMoveTracksRequest(Integer playlistPos, List<PlaylistItemReference> tracks_loop) {
        this.playlistPos = playlistPos;
        this.tracks_loop = tracks_loop;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }

    public List<PlaylistItemReference> getTracks_loop() {
        return tracks_loop;
    }

    public void setTracks_loop(List<PlaylistItemReference> tracks_loop) {
        this.tracks_loop = tracks_loop;
    }
}
