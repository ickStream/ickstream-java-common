/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.protocol.service.playlists;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSetTracksRequest {
    private String playlistId;
    private String playlistName;
    private Boolean overwrite;
    private Integer playlistPos;
    private List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    public PlaylistSetTracksRequest() {
    }

    public PlaylistSetTracksRequest(String playlistId, String playlistName) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
    }

    public PlaylistSetTracksRequest(String playlistId, String playlistName, Integer playlistPos, List<PlaylistItem> items) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.playlistPos = playlistPos;
        this.items = items;
    }

    public PlaylistSetTracksRequest(String playlistId, String playlistName, Boolean overwrite) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.overwrite = overwrite;
    }

    public PlaylistSetTracksRequest(String playlistId, String playlistName, Boolean overwrite, Integer playlistPos, List<PlaylistItem> items) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.overwrite = overwrite;
        this.playlistPos = playlistPos;
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

    public Integer getPlaylistPos() {
        return playlistPos;
    }

    public void setPlaylistPos(Integer playlistPos) {
        this.playlistPos = playlistPos;
    }

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }
}
