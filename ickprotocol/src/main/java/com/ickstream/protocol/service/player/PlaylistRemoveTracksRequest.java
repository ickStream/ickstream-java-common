/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
