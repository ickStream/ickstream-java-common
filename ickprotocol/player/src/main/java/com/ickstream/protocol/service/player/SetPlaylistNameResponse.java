/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class SetPlaylistNameResponse {
    private String playlistId;
    private String playlistName;
    private Integer countAll;

    public SetPlaylistNameResponse() {
    }

    public SetPlaylistNameResponse(String playlistId, String playlistName, Integer countAll) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.countAll = countAll;
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

    public Integer getCountAll() {
        return countAll;
    }

    public void setCountAll(Integer countAll) {
        this.countAll = countAll;
    }
}
