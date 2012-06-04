/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRemoveTracksRequest {
    private List<PlaylistItemReference> tracks_loop = new ArrayList<PlaylistItemReference>();

    public PlaylistRemoveTracksRequest() {
    }

    public PlaylistRemoveTracksRequest(List<PlaylistItemReference> tracks_loop) {
        this.tracks_loop = tracks_loop;
    }

    public List<PlaylistItemReference> getTracks_loop() {
        return tracks_loop;
    }

    public void setTracks_loop(List<PlaylistItemReference> tracks_loop) {
        this.tracks_loop = tracks_loop;
    }
}
