/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

public class PlaylistModificationResponse {
    private Boolean result;
    private String playlistId;

    public PlaylistModificationResponse() {
    }

    public PlaylistModificationResponse(Boolean result) {
        this.result = result;
    }

    public PlaylistModificationResponse(Boolean result, String playlistId) {
        this.result = result;
        this.playlistId = playlistId;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}
