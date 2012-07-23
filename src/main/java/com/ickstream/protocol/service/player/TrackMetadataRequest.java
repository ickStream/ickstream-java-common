/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;

public class TrackMetadataRequest {
    private Integer playlistPos;
    private Boolean replace;
    private PlaylistItem track;

    public TrackMetadataRequest() {
    }

    public TrackMetadataRequest(Integer playlistPos, Boolean replace, PlaylistItem track) {
        this.playlistPos = playlistPos;
        this.replace = replace;
        this.track = track;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }

    public Boolean getReplace() {
        return replace;
    }

    public void setReplace(Boolean replace) {
        this.replace = replace;
    }

    public PlaylistItem getTrack() {
        return track;
    }

    public void setTrack(PlaylistItem track) {
        this.track = track;
    }
}
