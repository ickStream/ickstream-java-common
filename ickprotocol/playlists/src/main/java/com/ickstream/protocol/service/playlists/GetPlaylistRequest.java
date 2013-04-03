/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

import com.ickstream.protocol.common.ChunkedRequest;

public class GetPlaylistRequest extends ChunkedRequest {
    private String playlistId;

    public GetPlaylistRequest(String playlistId) {
        this.playlistId = playlistId;
    }

    public GetPlaylistRequest(Integer offset, Integer count, String playlistId) {
        super(offset, count);
        this.playlistId = playlistId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}
