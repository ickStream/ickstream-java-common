/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaybackQueueRemoveTracksRequest {
    private List<PlaybackQueueItemReference> items = new ArrayList<PlaybackQueueItemReference>();

    public PlaybackQueueRemoveTracksRequest() {
    }

    public PlaybackQueueRemoveTracksRequest(List<PlaybackQueueItemReference> items) {
        this.items = items;
    }

    public List<PlaybackQueueItemReference> getItems() {
        return items;
    }

    public void setItems(List<PlaybackQueueItemReference> items) {
        this.items = items;
    }
}
