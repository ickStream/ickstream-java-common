/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.player;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.common.ickdiscovery.MessageSender;
import com.ickstream.protocol.common.DeviceStringMessageSender;

import java.util.HashMap;
import java.util.Map;

public class PlayerService extends AsyncJsonRpcClient implements JsonRpcResponseHandler, JsonRpcRequestHandler {
    private String deviceId;

    public PlayerService(MessageSender messageSender, String deviceId) {
        this(messageSender, deviceId, (Integer) null);
    }

    public PlayerService(MessageSender messageSender, String deviceId, IdProvider idProvider) {
        this(messageSender, deviceId, idProvider, null);
    }

    public PlayerService(MessageSender messageSender, String deviceId, Integer defaultTimeout) {
        this(messageSender, deviceId, null, defaultTimeout);
    }

    public PlayerService(MessageSender messageSender, String deviceId, IdProvider idProvider, Integer defaultTimeout) {
        super(new DeviceStringMessageSender(deviceId, messageSender), idProvider, defaultTimeout);
        this.deviceId = deviceId;
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((DeviceStringMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void setPlayerConfiguration(PlayerConfigurationRequest request, MessageHandler<PlayerConfigurationResponse> messageHandler) {
        setPlayerConfiguration(request, messageHandler, (Integer) null);
    }

    public void setPlayerConfiguration(PlayerConfigurationRequest request, MessageHandler<PlayerConfigurationResponse> messageHandler, Integer timeout) {
        sendRequest("setPlayerConfiguration", request, PlayerConfigurationResponse.class, messageHandler, timeout);
    }

    public void getPlayerConfiguration(MessageHandler<PlayerConfigurationResponse> messageHandler) {
        getPlayerConfiguration(messageHandler, (Integer) null);
    }

    public void getPlayerConfiguration(MessageHandler<PlayerConfigurationResponse> messageHandler, Integer timeout) {
        sendRequest("getPlayerConfiguration", null, PlayerConfigurationResponse.class, messageHandler, timeout);
    }

    public void getPlayerStatus(MessageHandler<PlayerStatusResponse> messageHandler) {
        getPlayerStatus(messageHandler, (Integer) null);
    }

    public void getPlayerStatus(MessageHandler<PlayerStatusResponse> messageHandler, Integer timeout) {
        sendRequest("getPlayerStatus", null, PlayerStatusResponse.class, messageHandler, timeout);
    }

    public void play(Boolean play) {
        play(play, (Integer) null);
    }

    public void play(Boolean play, Integer timeout) {
        Map<String, Boolean> parameters = new HashMap<String, Boolean>();
        parameters.put("playing", play);
        sendRequest("play", parameters, null, null, timeout);
    }

    public void getSeekPosition(MessageHandler<SeekPosition> messageHandler) {
        getSeekPosition(messageHandler, (Integer) null);
    }

    public void getSeekPosition(MessageHandler<SeekPosition> messageHandler, Integer timeout) {
        sendRequest("getSeekPosition", null, SeekPosition.class, messageHandler, timeout);
    }

    public void setSeekPosition(SeekPosition request, MessageHandler<SeekPosition> messageHandler) {
        setSeekPosition(request, messageHandler, (Integer) null);
    }

    public void setSeekPosition(SeekPosition request, MessageHandler<SeekPosition> messageHandler, Integer timeout) {
        sendRequest("setSeekPosition", request, SeekPosition.class, messageHandler, timeout);
    }

    public void getTrack(Integer playlistPos, MessageHandler<TrackResponse> messageHandler) {
        getTrack(playlistPos, messageHandler, (Integer) null);
    }

    public void getTrack(Integer playlistPos, MessageHandler<TrackResponse> messageHandler, Integer timeout) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        sendRequest("getTrack", parameters, TrackResponse.class, messageHandler, timeout);
    }

    public void setTrack(Integer playlistPos, MessageHandler<SetTrackResponse> messageHandler) {
        setTrack(playlistPos, messageHandler, (Integer) null);
    }

    public void setTrack(Integer playlistPos, MessageHandler<SetTrackResponse> messageHandler, Integer timeout) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        sendRequest("setTrack", parameters, SetTrackResponse.class, messageHandler, timeout);
    }

    public void setTrackMetadata(TrackMetadataRequest request, MessageHandler<TrackResponse> messageHandler) {
        setTrackMetadata(request, messageHandler, (Integer) null);
    }

    public void setTrackMetadata(TrackMetadataRequest request, MessageHandler<TrackResponse> messageHandler, Integer timeout) {
        sendRequest("setTrackMetadata", request, TrackResponse.class, messageHandler, timeout);
    }

    public void getVolume(MessageHandler<VolumeResponse> messageHandler) {
        getVolume(messageHandler, (Integer) null);
    }

    public void getVolume(MessageHandler<VolumeResponse> messageHandler, Integer timeout) {
        sendRequest("getVolume", null, VolumeResponse.class, messageHandler, timeout);
    }

    public void setVolume(VolumeRequest request, MessageHandler<VolumeResponse> messageHandler) {
        setVolume(request, messageHandler, (Integer) null);
    }

    public void setVolume(VolumeRequest request, MessageHandler<VolumeResponse> messageHandler, Integer timeout) {
        sendRequest("setVolume", request, VolumeResponse.class, messageHandler, timeout);
    }

    public void setPlaylistName(SetPlaylistNameRequest request, MessageHandler<SetPlaylistNameResponse> messagesHandler) {
        setPlaylistName(request, messagesHandler, (Integer) null);
    }

    public void setPlaylistName(SetPlaylistNameRequest request, MessageHandler<SetPlaylistNameResponse> messagesHandler, Integer timeout) {
        sendRequest("setPlaylistName", request, SetPlaylistNameResponse.class, messagesHandler, timeout);
    }

    public void getPlaylist(ChunkedRequest request, MessageHandler<PlaylistResponse> messageHandler) {
        getPlaylist(request, messageHandler, (Integer) null);
    }

    public void getPlaylist(ChunkedRequest request, MessageHandler<PlaylistResponse> messageHandler, Integer timeout) {
        sendRequest("getPlaylist", request, PlaylistResponse.class, messageHandler, timeout);
    }

    public void addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        addTracks(request, messageHandler, (Integer) null);
    }

    public void addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("addTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public void removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        removeTracks(request, messageHandler, (Integer) null);
    }

    public void removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("removeTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public void moveTracks(PlaylistMoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        moveTracks(request, messageHandler, (Integer) null);
    }

    public void moveTracks(PlaylistMoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("moveTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public void setTracks(PlaylistSetTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        setTracks(request, messageHandler, (Integer) null);
    }

    public void setTracks(PlaylistSetTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("setTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public void addPlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> listener) {
        addNotificationListener("playerStatusChanged", PlayerStatusResponse.class, listener);
    }

    public void removePlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> listener) {
        removeNotificationListener("playerStatusChanged", listener);
    }

    public void addPlaylistChangedListener(MessageHandler<PlaylistChangedNotification> listener) {
        addNotificationListener("playlistChanged", PlaylistChangedNotification.class, listener);
    }

    public void removePlaylistChangedListener(MessageHandler<PlaylistChangedNotification> listener) {
        removeNotificationListener("playlistChanged", listener);
    }
}
