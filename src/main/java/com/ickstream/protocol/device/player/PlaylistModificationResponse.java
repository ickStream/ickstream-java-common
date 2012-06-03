/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

public class PlaylistModificationResponse {
    private Boolean result;
    private Integer playlistPos;

    public PlaylistModificationResponse() {
    }

    public PlaylistModificationResponse(Boolean result, Integer playlistPos) {
        this.result = result;
        this.playlistPos = playlistPos;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }
}
