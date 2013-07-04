/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.ArrayList;
import java.util.List;

public class PlaybackQueueSetTracksRequest {
    private String playlistId;
    private String playlistName;
    private Integer playbackQueuePos;
    private List<PlaybackQueueItem> items = new ArrayList<PlaybackQueueItem>();

    public PlaybackQueueSetTracksRequest() {
    }

    public PlaybackQueueSetTracksRequest(String playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public PlaybackQueueSetTracksRequest(String playlistId, String playlistName, Integer playbackQueuePos, List<PlaybackQueueItem> items) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.playbackQueuePos = playbackQueuePos;
        this.items = items;
    }

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
