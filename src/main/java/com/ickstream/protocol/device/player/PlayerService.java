/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.JsonRpcRequest;
import com.ickstream.protocol.JsonRpcResponse;
import com.ickstream.protocol.JsonRpcResponseHandler;
import com.ickstream.protocol.device.MessageSender;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerService implements JsonRpcResponseHandler {
    private Integer id = 1;
    private MessageSender messageSender;
    private String deviceId;
    private ObjectMapper mapper;
    private final Map<Integer, MessageHandler> messageHandlers = new HashMap<Integer, MessageHandler>();
    private final Map<Integer, Class> messageHandlerTypes = new HashMap<Integer, Class>();

    public PlayerService(MessageSender messageSender, String deviceId) {
        this.messageSender = messageSender;
        this.deviceId = deviceId;
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public Integer setPlayerConfiguration(PlayerConfigurationRequest request, MessageHandler<PlayerConfigurationResponse> messageHandler) {
        return sendRequest("setPlayerConfiguration", request, PlayerConfigurationResponse.class, messageHandler);
    }

    public Integer getPlaylist(ChunkedRequest request, MessageHandler<PlaylistResponse> messageHandler) {
        return sendRequest("getPlaylist", request, PlaylistResponse.class, messageHandler);
    }

    public Integer play(Boolean play) {
        Map<String, Boolean> parameters = new HashMap<String, Boolean>();
        parameters.put("play", play);
        return sendRequest("play", parameters, null, null);
    }

    public Integer addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("addTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public Integer removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("removeTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public Integer setTrack(Integer playlistPos, MessageHandler<SetTrackResponse> messageHandler) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        return sendRequest("setTrack", parameters, SetTrackResponse.class, messageHandler);
    }

    private Integer sendRequest(String method, Object params, Class messageResponseClass, MessageHandler messageHandler) {
        synchronized (messageHandlers) {
            if (messageResponseClass != null && messageHandler != null) {
                messageHandlers.put(id, messageHandler);
                messageHandlerTypes.put(id, messageResponseClass);
            }
            JsonRpcRequest jsonRpcRequest = new JsonRpcRequest();
            jsonRpcRequest.setId(id++);
            jsonRpcRequest.setMethod(method);
            if (params != null) {
                jsonRpcRequest.setParams(mapper.valueToTree(params));
            } else {
                jsonRpcRequest.setParams(mapper.valueToTree(new HashMap()));
            }
            try {
                String requestString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.valueToTree(jsonRpcRequest));
                System.out.println("SENDING Device REQUEST (" + deviceId + "):\n" + requestString + "\n");
                messageSender.sendMessage(deviceId, requestString);
                return jsonRpcRequest.getId();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onResponse(JsonRpcResponse message) {
        MessageHandler messageHandler = messageHandlers.get(message.getId());
        Class responseType = messageHandlerTypes.get(message.getId());
        if (messageHandler != null) {
            try {
                Object parameters = null;
                if (responseType != null) {
                    parameters = mapper.treeToValue(message.getResult(), responseType);
                }
                messageHandlers.get(message.getId()).onMessage(parameters);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
