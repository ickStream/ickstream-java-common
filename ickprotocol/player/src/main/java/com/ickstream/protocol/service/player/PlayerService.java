/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import com.ickstream.common.ickdiscovery.MessageSender;
import com.ickstream.common.ickdiscovery.ServiceType;
import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.DeviceStringMessageSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Client class for accessing Player service
 * <p/>
 * See the official API documentation for details regarding individual methods and parameters.
 */
public class PlayerService extends AsyncJsonRpcClient implements JsonRpcResponseHandler, JsonRpcRequestHandler {
    private String deviceId;

    /**
     * Create a client for a specific device implementing the Player protocol
     *
     * @param messageSender The message sender to use when communicating with the device
     * @param deviceId      The device identity of the device
     */
    public PlayerService(MessageSender messageSender, String deviceId) {
        this(messageSender, deviceId, (Integer) null);
    }

    /**
     * Create a client for a specific device implementing the Player protocol
     *
     * @param messageSender The message sender to use when communicating with the device
     * @param deviceId      The device identity of the device
     * @param idProvider    The identity provider to use to generate identities for JSON-RPC requests
     */
    public PlayerService(MessageSender messageSender, String deviceId, IdProvider idProvider) {
        this(messageSender, deviceId, idProvider, null);
    }

    /**
     * Create a client for a specific device implementing the Player protocol
     *
     * @param messageSender  The message sender to use when communicating with the device
     * @param deviceId       The device identity of the device
     * @param defaultTimeout The default timeout to use when no explicit timeout has been specified in a method call
     */
    public PlayerService(MessageSender messageSender, String deviceId, Integer defaultTimeout) {
        this(messageSender, deviceId, null, defaultTimeout);
    }

    /**
     * Create a client for a specific device implementing the Player protocol
     *
     * @param messageSender  The message sender to use when communicating with the device
     * @param deviceId       The device identity of the device
     * @param idProvider     The identity provider to use to generate identities for JSON-RPC requests
     * @param defaultTimeout The default timeout to use when no explicit timeout has been specified in a method call
     */
    public PlayerService(MessageSender messageSender, String deviceId, IdProvider idProvider, Integer defaultTimeout) {
        super(new DeviceStringMessageSender(deviceId, ServiceType.PLAYER, messageSender), idProvider, defaultTimeout);
        this.deviceId = deviceId;
    }

    /**
     * Set message logger to be used for logging outgoing messages through this client
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        ((DeviceStringMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    public void getProtocolVersions(MessageHandler<ProtocolVersionsResponse> messageHandler) {
        getProtocolVersions(messageHandler, (Integer) null);
    }

    public void getProtocolVersions(MessageHandler<ProtocolVersionsResponse> messageHandler, Integer timeout) {
        sendRequest("getProtocolVersions", null, ProtocolVersionsResponse.class, messageHandler, timeout);
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

    public void getTrack(Integer playbackQueuePos, MessageHandler<TrackResponse> messageHandler) {
        getTrack(playbackQueuePos, messageHandler, (Integer) null);
    }

    public void getTrack(Integer playbackQueuePos, MessageHandler<TrackResponse> messageHandler, Integer timeout) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playbackQueuePos", playbackQueuePos);
        sendRequest("getTrack", parameters, TrackResponse.class, messageHandler, timeout);
    }

    public void setTrack(Integer playbackQueuePos, MessageHandler<SetTrackResponse> messageHandler) {
        setTrack(playbackQueuePos, messageHandler, (Integer) null);
    }

    public void setTrack(Integer playbackQueuePos, MessageHandler<SetTrackResponse> messageHandler, Integer timeout) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playbackQueuePos", playbackQueuePos);
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

    public void getPlaybackQueue(PlaybackQueueRequest request, MessageHandler<PlaybackQueueResponse> messageHandler) {
        getPlaybackQueue(request, messageHandler, (Integer) null);
    }

    public void getPlaybackQueue(PlaybackQueueRequest request, MessageHandler<PlaybackQueueResponse> messageHandler, Integer timeout) {
        sendRequest("getPlaybackQueue", request, PlaybackQueueResponse.class, messageHandler, timeout);
    }

    public void addTracks(PlaybackQueueAddTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler) {
        addTracks(request, messageHandler, (Integer) null);
    }

    public void addTracks(PlaybackQueueAddTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler, Integer timeout) {
        sendRequest("addTracks", request, PlaybackQueueModificationResponse.class, messageHandler, timeout);
    }

    public void removeTracks(PlaybackQueueRemoveTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler) {
        removeTracks(request, messageHandler, (Integer) null);
    }

    public void removeTracks(PlaybackQueueRemoveTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler, Integer timeout) {
        sendRequest("removeTracks", request, PlaybackQueueModificationResponse.class, messageHandler, timeout);
    }

    public void moveTracks(PlaybackQueueMoveTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler) {
        moveTracks(request, messageHandler, (Integer) null);
    }

    public void moveTracks(PlaybackQueueMoveTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler, Integer timeout) {
        sendRequest("moveTracks", request, PlaybackQueueModificationResponse.class, messageHandler, timeout);
    }

    public void setTracks(PlaybackQueueSetTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler) {
        setTracks(request, messageHandler, (Integer) null);
    }

    public void setTracks(PlaybackQueueSetTracksRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler, Integer timeout) {
        sendRequest("setTracks", request, PlaybackQueueModificationResponse.class, messageHandler, timeout);
    }

    public void setPlaybackQueueMode(PlaybackQueueModeRequest request, MessageHandler<PlaybackQueueModeResponse> messageHandler) {
        setPlaybackQueueMode(request, messageHandler, (Integer) null);
    }

    public void setPlaybackQueueMode(PlaybackQueueModeRequest request, MessageHandler<PlaybackQueueModeResponse> messageHandler, Integer timeout) {
        sendRequest("setPlaybackQueueMode", request, PlaybackQueueModeResponse.class, messageHandler, timeout);
    }

    public void shuffleTracks(MessageHandler<PlaybackQueueModificationResponse> messageHandler) {
        shuffleTracks(messageHandler, (Integer) null);
    }

    public void shuffleTracks(MessageHandler<PlaybackQueueModificationResponse> messageHandler, Integer timeout) {
        sendRequest("shuffleTracks", null, PlaybackQueueModificationResponse.class, messageHandler, timeout);
    }

    public void setDynamicPlaybackQueueParameters(DynamicPlaybackQueueParametersRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler) {
        setDynamicPlaybackQueueParameters(request, messageHandler, (Integer) null);
    }

    public void setDynamicPlaybackQueueParameters(DynamicPlaybackQueueParametersRequest request, MessageHandler<PlaybackQueueModificationResponse> messageHandler, Integer timeout) {
        sendRequest("setDynamicPlaybackQueueParameters", request, PlaybackQueueModificationResponse.class, messageHandler, timeout);
    }

    public void addPlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> listener) {
        addNotificationListener("playerStatusChanged", PlayerStatusResponse.class, listener);
    }

    public void removePlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> listener) {
        removeNotificationListener("playerStatusChanged", listener);
    }

    public void addPlaybackQueueChangedListener(MessageHandler<PlaybackQueueChangedNotification> listener) {
        addNotificationListener("playbackQueueChanged", PlaybackQueueChangedNotification.class, listener);
    }

    public void removePlaybackQueueChangedListener(MessageHandler<PlaybackQueueChangedNotification> listener) {
        removeNotificationListener("playbackQueueChanged", listener);
    }
}
