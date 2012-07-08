/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.device.MessageSender;
import com.ickstream.common.jsonrpc.MessageHandler;

import java.util.HashMap;
import java.util.Map;

public class PlayerService extends AsyncJsonRpcClient implements JsonRpcResponseHandler, JsonRpcRequestHandler {
    private Integer id = 1;

    private static class DeviceMessageSender implements com.ickstream.common.jsonrpc.MessageSender {
        private String deviceId;
        private MessageSender messageSender;
        private MessageLogger messageLogger;

        private DeviceMessageSender(String deviceId, MessageSender messageSender) {
            this.deviceId = deviceId;
            this.messageSender = messageSender;
        }

        public void setMessageLogger(MessageLogger messageLogger) {
            this.messageLogger = messageLogger;
        }

        @Override
        public void sendMessage(String message) {
            if(messageLogger != null) {
                messageLogger.onOutgoingMessage(deviceId,message);
            }
            messageSender.sendMessage(deviceId, message);
        }
    }

    public PlayerService(MessageSender messageSender, String deviceId) {
        super(new DeviceMessageSender(deviceId, messageSender));
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((DeviceMessageSender)getMessageSender()).setMessageLogger(messageLogger);
    }

    public String setPlayerConfiguration(PlayerConfigurationRequest request, MessageHandler<PlayerConfigurationResponse> messageHandler) {
        return sendRequest("setPlayerConfiguration", request, PlayerConfigurationResponse.class, messageHandler);
    }

    public String getPlayerConfiguration(MessageHandler<PlayerConfigurationResponse> messageHandler) {
        return sendRequest("getPlayerConfiguration", null, PlayerConfigurationResponse.class, messageHandler);
    }

    public String getPlayerStatus(MessageHandler<PlayerStatusResponse> messageHandler) {
        return sendRequest("getPlayerStatus", null, PlayerStatusResponse.class, messageHandler);
    }

    public String play(Boolean play) {
        Map<String, Boolean> parameters = new HashMap<String, Boolean>();
        parameters.put("playing", play);
        return sendRequest("play", parameters, null, null);
    }

    public String getSeekPosition(MessageHandler<SeekPosition> messageHandler) {
        return sendRequest("getSeekPosition", null, SeekPosition.class, messageHandler);
    }

    public String setSeekPosition(SeekPosition request, MessageHandler<SeekPosition> messageHandler) {
        return sendRequest("setSeekPosition", request, SeekPosition.class, messageHandler);
    }

    public String getTrack(Integer playlistPos, MessageHandler<TrackResponse> messageHandler) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        return sendRequest("getTrack", parameters, TrackResponse.class, messageHandler);
    }

    public String setTrack(Integer playlistPos, MessageHandler<SetTrackResponse> messageHandler) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        return sendRequest("setTrack", parameters, SetTrackResponse.class, messageHandler);
    }

    public String setTrackMetadata(TrackMetadataRequest request, MessageHandler<TrackResponse> messageHandler) {
        return sendRequest("setTrackMetadata", request, TrackResponse.class, messageHandler);
    }

    public String getVolume(MessageHandler<VolumeResponse> messageHandler) {
        return sendRequest("getVolume", null, VolumeResponse.class, messageHandler);
    }

    public String setVolume(VolumeRequest request, MessageHandler<VolumeResponse> messageHandler) {
        return sendRequest("setVolume", request, VolumeResponse.class, messageHandler);
    }

    public String setPlaylistName(SetPlaylistNameRequest request, MessageHandler<SetPlaylistNameResponse> messagesHandler) {
        return sendRequest("setPlaylistName", request, SetPlaylistNameResponse.class, messagesHandler);
    }

    public String getPlaylist(ChunkedRequest request, MessageHandler<PlaylistResponse> messageHandler) {
        return sendRequest("getPlaylist", request, PlaylistResponse.class, messageHandler);
    }

    public String addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("addTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public String removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("removeTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public String moveTracks(PlaylistMoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("moveTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public String setTracks(PlaylistSetTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("setTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public void addPlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> messageHandler) {
        addNotificationListener("playerStatusChanged", PlayerStatusResponse.class, messageHandler);
    }

    public void removePlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> messageHandler) {
        removeNotificationListener("playerStatusChanged", messageHandler);
    }

    public void addPlaylistChangedListener(MessageHandler<PlaylistChangedNotification> messageHandler) {
        addNotificationListener("playlistChanged", PlaylistChangedNotification.class, messageHandler);
    }

    public void removePlaylistChangedListener(MessageHandler<PlaylistChangedNotification> messageHandler) {
        removeNotificationListener("playlistChanged", messageHandler);
    }
}
