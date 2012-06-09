/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import com.ickstream.protocol.*;
import com.ickstream.protocol.device.MessageSender;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerService implements JsonRpcResponseHandler, JsonRpcRequestHandler {
    private Integer id = 1;
    private MessageSender messageSender;
    private String deviceId;
    private ObjectMapper mapper;
    private final Map<Integer, MessageHandler> messageHandlers = new HashMap<Integer, MessageHandler>();
    private final Map<String, List<MessageHandler>> notificationHandlers = new HashMap<String, List<MessageHandler>>();
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

    public Integer getPlayerConfiguration(MessageHandler<PlayerConfigurationResponse> messageHandler) {
        return sendRequest("getPlayerConfiguration", null, PlayerConfigurationResponse.class, messageHandler);
    }

    public Integer getPlayerStatus(MessageHandler<PlayerStatusResponse> messageHandler) {
        return sendRequest("getPlayerStatus", null, PlayerStatusResponse.class, messageHandler);
    }

    public Integer play(Boolean play) {
        Map<String, Boolean> parameters = new HashMap<String, Boolean>();
        parameters.put("play", play);
        return sendRequest("play", parameters, null, null);
    }

    public Integer getSeekPosition(MessageHandler<SeekPosition> messageHandler) {
        return sendRequest("getSeekPosition", null, SeekPosition.class, messageHandler);
    }

    public Integer setSeekPosition(SeekPosition request, MessageHandler<SeekPosition> messageHandler) {
        return sendRequest("setSeekPosition", request, SeekPosition.class, messageHandler);
    }

    public Integer getTrack(Integer playlistPos, MessageHandler<TrackResponse> messageHandler) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        return sendRequest("getTrack", parameters, TrackResponse.class, messageHandler);
    }

    public Integer setTrack(Integer playlistPos, MessageHandler<SetTrackResponse> messageHandler) {
        Map<String, Integer> parameters = new HashMap<String, Integer>();
        parameters.put("playlistPos", playlistPos);
        return sendRequest("setTrack", parameters, SetTrackResponse.class, messageHandler);
    }

    public Integer setTrackMetadata(TrackMetadataRequest request, MessageHandler<TrackResponse> messageHandler) {
        return sendRequest("setTrackMetadata", request, TrackResponse.class, messageHandler);
    }

    public Integer getVolume(MessageHandler<VolumeResponse> messageHandler) {
        return sendRequest("getVolume", null, VolumeResponse.class, messageHandler);
    }

    public Integer setVolume(VolumeRequest request, MessageHandler<VolumeResponse> messageHandler) {
        return sendRequest("setVolume", request, VolumeResponse.class, messageHandler);
    }

    public Integer getPlaylist(ChunkedRequest request, MessageHandler<PlaylistResponse> messageHandler) {
        return sendRequest("getPlaylist", request, PlaylistResponse.class, messageHandler);
    }


    public Integer addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("addTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public Integer removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("removeTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public Integer moveTracks(PlaylistMoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("moveTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public Integer setTracks(PlaylistSetTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        return sendRequest("setTracks", request, PlaylistModificationResponse.class, messageHandler);
    }

    public void addPlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> messageHandler) {
        synchronized (notificationHandlers) {
            if (!notificationHandlers.containsKey("playerStatusChanged")) {
                notificationHandlers.put("playerStatusChanged", new ArrayList<MessageHandler>());
            }
            notificationHandlers.get("playerStatusChanged").add(messageHandler);
        }
    }

    public void removePlayerStatusChangedListener(MessageHandler<PlayerStatusResponse> messageHandler) {
        synchronized (notificationHandlers) {
            if (notificationHandlers.containsKey("playerStatusChanged")) {
                notificationHandlers.get("playerStatusChanged").remove(messageHandler);
            }
        }
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
    public void onRequest(JsonRpcRequest message) {
        try {
            List<MessageHandler> notificationHandlers = this.notificationHandlers.get(message.getMethod());
            Object parameters = null;
            if (notificationHandlers != null && message.getMethod().equals("playerStatusChanged")) {
                if (message.getParams() != null) {
                    parameters = mapper.treeToValue(message.getParams(), PlayerStatusResponse.class);
                    for (MessageHandler notificationHandler : notificationHandlers) {
                        notificationHandler.onMessage(parameters);
                    }
                }
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
