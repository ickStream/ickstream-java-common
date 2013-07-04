/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class TrackResponse {
    private String playlistId;
    private String playlistName;
    private Integer playbackQueuePos;
    private PlaybackQueueItem track;

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

    public PlaybackQueueItem getTrack() {
        return track;
    }

    public void setTrack(PlaybackQueueItem track) {
        this.track = track;
    }
}
