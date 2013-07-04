/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaybackQueueMoveTracksRequest {
    private Integer playbackQueuePos;
    private List<PlaybackQueueItemReference> items = new ArrayList<PlaybackQueueItemReference>();

    public PlaybackQueueMoveTracksRequest() {
    }

    public PlaybackQueueMoveTracksRequest(Integer playbackQueuePos, List<PlaybackQueueItemReference> items) {
        this.playbackQueuePos = playbackQueuePos;
        this.items = items;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }

    public List<PlaybackQueueItemReference> getItems() {
        return items;
    }

    public void setItems(List<PlaybackQueueItemReference> items) {
        this.items = items;
    }
}
