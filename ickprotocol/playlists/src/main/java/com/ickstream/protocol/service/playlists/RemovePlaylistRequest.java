package com.ickstream.protocol.service.playlists;

public class RemovePlaylistRequest {
    private String playlistId;

    public RemovePlaylistRequest(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}
