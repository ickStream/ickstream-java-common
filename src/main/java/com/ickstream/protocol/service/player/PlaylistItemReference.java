/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;


public class PlaylistItemReference {
    private String id;
    private Integer playlistPos;

    public PlaylistItemReference() {
    }

    public PlaylistItemReference(String id) {
        this.id = id;
    }

    public PlaylistItemReference(String id, Integer playlistPos) {
        this.id = id;
        this.playlistPos = playlistPos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }
}
