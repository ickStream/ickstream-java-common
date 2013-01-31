/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public class SeekPosition {
    private Integer playlistPos;
    private Double seekPos;

    public SeekPosition() {
    }

    public SeekPosition(Integer playlistPos, Double seekPos) {
        this.playlistPos = playlistPos;
        this.seekPos = seekPos;
    }

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }

    public Double getSeekPos() {
        return seekPos;
    }

    public void setSeekPos(Double seekPos) {
        this.seekPos = seekPos;
    }
}
