/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRemoveTracksRequest {
    private List<PlaylistItemReference> items = new ArrayList<PlaylistItemReference>();

    public PlaylistRemoveTracksRequest() {
    }

    public PlaylistRemoveTracksRequest(List<PlaylistItemReference> items) {
        this.items = items;
    }

    public List<PlaylistItemReference> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItemReference> items) {
        this.items = items;
    }
}
