/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaybackQueueAddTracksRequest {
    private Integer playbackQueuePos;
    private List<PlaybackQueueItem> items = new ArrayList<PlaybackQueueItem>();

    public PlaybackQueueAddTracksRequest() {
    }

    public PlaybackQueueAddTracksRequest(List<PlaybackQueueItem> tracks_loop) {
        this.items = tracks_loop;
    }

    public PlaybackQueueAddTracksRequest(Integer playbackQueuePos, List<PlaybackQueueItem> items) {
        this.playbackQueuePos = playbackQueuePos;
        this.items = items;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        this.playbackQueuePos = playbackQueuePos;
    }

    public List<PlaybackQueueItem> getItems() {
        return items;
    }

    public void setItems(List<PlaybackQueueItem> items) {
        this.items = items;
    }
}
