/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;


import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class FindPlaylistsResponse extends ChunkedResponse {
    private List<Playlist> items = new ArrayList<Playlist>();

    public List<Playlist> getItems() {
        return items;
    }

    public void setItems(List<Playlist> items) {
        this.items = items;
    }
}
