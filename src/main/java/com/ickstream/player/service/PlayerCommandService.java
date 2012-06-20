/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.service;

import com.ickstream.player.model.PlayerStatus;
import com.ickstream.protocol.ChunkedRequest;
import com.ickstream.protocol.device.player.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerCommandService extends AbstractJsonRpcService {
    private PlayerStatus playerStatus;
    private PlayerManager player;
    private PlayerNotificationSender notificationSender;

    public PlayerCommandService(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public PlayerCommandService(PlayerStatus playerStatus, PlayerNotificationSender notificationSender) {
        this.playerStatus = playerStatus;
        this.notificationSender = notificationSender;
    }

    public PlayerCommandService(PlayerManager player, PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        this.player = player;
    }

    public PlayerCommandService(PlayerManager player, PlayerNotificationSender notificationSender, PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        this.notificationSender = notificationSender;
        this.player = player;
    }

    public PlayerConfigurationResponse setPlayerConfiguration(PlayerConfigurationRequest configuration) {
        if (configuration.getAccessToken() != null) {
            player.setAccessToken(configuration.getAccessToken());
        }
        if (configuration.getPlayerName() != null) {
            player.setName(configuration.getPlayerName());
        }
        PlayerConfigurationResponse response = new PlayerConfigurationResponse();
        response.setPlayerName(player.getName());
        response.setHardwareId(player.getHardwareId());
        return response;
    }

    public PlayerConfigurationResponse getPlayerConfiguration() {
        PlayerConfigurationResponse response = new PlayerConfigurationResponse();
        response.setPlayerName(player.getName());
        response.setHardwareId(player.getHardwareId());
        return response;
    }

    public PlayerStatusResponse getPlayerStatus() {
        PlayerStatusResponse response = new PlayerStatusResponse();
        response.setPlaying(playerStatus.getPlaying());
        response.setPlaylistId(playerStatus.getPlaylist().getId());
        response.setPlaylistName(playerStatus.getPlaylist().getName());
        response.setPlaylistPos(playerStatus.getPlaylistPos());
        if (player != null && playerStatus.getPlaylistPos() != null && playerStatus.getPlaying()) {
            playerStatus.setSeekPos(player.getSeekPosition());
        }
        response.setSeekPos(playerStatus.getSeekPos());
        if (playerStatus.getPlaylist().getItems().size() > 0) {
            response.setTrack(playerStatus.getPlaylist().getItems().get(playerStatus.getPlaylistPos()));
        }
        return response;
    }

    public PlaylistResponse getPlaylist(ChunkedRequest request) {
        PlaylistResponse response = new PlaylistResponse();
        response.setPlaylistId(playerStatus.getPlaylist().getId());
        response.setPlaylistName(playerStatus.getPlaylist().getName());
        Integer offset = request.getOffset() != null ? request.getOffset() : 0;
        Integer count = request.getCount() != null ? request.getCount() : playerStatus.getPlaylist().getItems().size();
        response.setOffset(offset);
        response.setCountAll(playerStatus.getPlaylist().getItems().size());
        if (offset < playerStatus.getPlaylist().getItems().size()) {
            if (offset + count > playerStatus.getPlaylist().getItems().size()) {
                response.setTracks_loop(playerStatus.getPlaylist().getItems().subList(offset, playerStatus.getPlaylist().getItems().size()));
            } else {
                response.setTracks_loop(playerStatus.getPlaylist().getItems().subList(offset, offset + count));
            }
        } else {
            response.setTracks_loop(playerStatus.getPlaylist().getItems().subList(offset, playerStatus.getPlaylist().getItems().size()));
        }
        return response;
    }

    public PlaylistModificationResponse addTracks(PlaylistAddTracksRequest request) {
        if (request.getPlaylistPos() != null) {
            // Insert tracks in middle
            playerStatus.getPlaylist().getItems().addAll(request.getPlaylistPos(), request.getTracks_loop());
            if (playerStatus.getPlaylistPos() != null && playerStatus.getPlaylistPos() >= request.getPlaylistPos()) {
                playerStatus.setPlaylistPos(playerStatus.getPlaylistPos() + request.getTracks_loop().size());
            }
        } else {
            // Add tracks at end
            playerStatus.getPlaylist().getItems().addAll(request.getTracks_loop());
        }
        // Set playlist position to first track if there weren't any tracks in the playlist before
        if (playerStatus.getPlaylistPos() == null) {
            playerStatus.setPlaylistPos(0);
        }
        sendPlaylistChangedNotification();
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }

    public PlaylistModificationResponse removeTracks(PlaylistRemoveTracksRequest request) {

        List<PlaylistItem> modifiedPlaylist = new ArrayList<PlaylistItem>(playerStatus.getPlaylist().getItems());
        int modifiedPlaylistPos = playerStatus.getPlaylistPos();
        boolean affectsPlayback = false;
        for (PlaylistItemReference itemReference : request.getTracks_loop()) {
            if (itemReference.getPlaylistPos() != null) {
                PlaylistItem item = playerStatus.getPlaylist().getItems().get(itemReference.getPlaylistPos());
                if (item.getId().equals(itemReference.getId())) {
                    if (itemReference.getPlaylistPos() < playerStatus.getPlaylistPos()) {
                        modifiedPlaylistPos--;
                    } else if (itemReference.getPlaylistPos().equals(playerStatus.getPlaylistPos())) {
                        affectsPlayback = true;
                    }
                    modifiedPlaylist.remove(item);
                } else {
                    throw new IllegalArgumentException("Track identity and playlist position doesn't match (trackId=" + itemReference.getId() + ", playlistPos=" + itemReference.getPlaylistPos() + ")");
                }
            } else {
                int i = 0;
                for (Iterator<PlaylistItem> it = modifiedPlaylist.iterator(); it.hasNext(); i++) {
                    PlaylistItem item = it.next();
                    if (item.getId().equals(itemReference.getId())) {
                        if (i < modifiedPlaylistPos) {
                            modifiedPlaylistPos--;
                        } else if (i == modifiedPlaylistPos) {
                            affectsPlayback = true;
                        }
                        it.remove();
                    }
                }
            }
        }
        playerStatus.getPlaylist().setItems(modifiedPlaylist);
        if(!playerStatus.getPlaylistPos().equals(modifiedPlaylistPos)) {
            playerStatus.setPlaylistPos(modifiedPlaylistPos);
            if(playerStatus.getPlaying() && !affectsPlayback) {
                sendPlayerStatusChangedNotification();
            }
        }
        // Make sure we make the player aware that it should change track
        if (playerStatus.getPlaying() && affectsPlayback && player != null) {
            if (modifiedPlaylist.size() > 0) {
                player.play();
            } else {
                playerStatus.setPlaylistPos(null);
                playerStatus.setSeekPos(null);
                player.pause();
            }
        }
        sendPlaylistChangedNotification();
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }

    public PlaylistModificationResponse moveTracks(PlaylistMoveTracksRequest request) {
        throw new IllegalArgumentException("moveTracks method Not Implemented");
    }

    public PlaylistModificationResponse setTracks(PlaylistSetTracksRequest request) {
        playerStatus.getPlaylist().setId(request.getPlaylistId());
        playerStatus.getPlaylist().setName(request.getPlaylistName());
        playerStatus.getPlaylist().setItems(request.getTracks_loop());

        Integer playlistPos = request.getPlaylistPos() != null ? request.getPlaylistPos() : 0;
        if (request.getTracks_loop().size() > 0) {
            setTrack(playlistPos);
        } else {
            playerStatus.setSeekPos(null);
            playerStatus.setPlaylistPos(null);
            if (player != null && playerStatus.getPlaying()) {
                player.pause();
            }
        }
        sendPlaylistChangedNotification();
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }


    @ResultName("playing")
    public Boolean play(@ParamName("playing") Boolean play) {
        if (playerStatus.getPlaylistPos() != null && play != null) {
            if (!playerStatus.getPlaying() && play) {
                if (player == null || player.play()) {
                    playerStatus.setPlaying(true);
                }
            } else if (playerStatus.getPlaying() && !play) {
                if (player == null || player.pause()) {
                    playerStatus.setPlaying(false);
                }
            }
        }
        return playerStatus.getPlaying();
    }

    public SeekPosition getSeekPosition() {
        SeekPosition response = new SeekPosition();
        response.setPlaylistPos(playerStatus.getPlaylistPos());
        if (player != null && playerStatus.getPlaylistPos() != null && playerStatus.getPlaying()) {
            playerStatus.setSeekPos(player.getSeekPosition());
        }
        response.setSeekPos(playerStatus.getSeekPos());
        return response;
    }

    public SeekPosition setSeekPosition(SeekPosition request) {
        if (request.getPlaylistPos() != null && playerStatus.getPlaylist().getItems().size() > request.getPlaylistPos()) {
            playerStatus.setPlaylistPos(request.getPlaylistPos());
            Double seekPosition = request.getSeekPos() != null ? request.getSeekPos() : 0;
            //TODO: Handle logic regarding seek position and length of track
            playerStatus.setSeekPos(seekPosition);
            return getSeekPosition();
        } else {
            throw new IllegalArgumentException("Invalid playlist position specified");
        }
    }

    public TrackResponse getTrack(@ParamName("playlistPos") Integer playlistPos) {
        TrackResponse response = new TrackResponse();
        response.setPlaylistId(playerStatus.getPlaylist().getId());
        response.setPlaylistName(playerStatus.getPlaylist().getName());
        if (playlistPos != null && playlistPos < playerStatus.getPlaylist().getItems().size()) {
            response.setPlaylistPos(playlistPos);
            response.setTrack(playerStatus.getPlaylist().getItems().get(playlistPos));
        } else if (playlistPos == null && playerStatus.getPlaylistPos() != null) {
            response.setPlaylistPos(playerStatus.getPlaylistPos());
            response.setTrack(playerStatus.getPlaylist().getItems().get(playerStatus.getPlaylistPos()));
        }
        return response;
    }

    @ResultName("playlistPos")
    public Integer setTrack(@ParamName("playlistPos") Integer playlistPos) {
        if (playlistPos != null && playlistPos < playerStatus.getPlaylist().getItems().size()) {
            playerStatus.setPlaylistPos(playlistPos);
            playerStatus.setSeekPos(0d);
            // Make sure we make the player aware that it should change track
            if (playerStatus.getPlaying() && player != null) {
                player.play();
            }
            return playlistPos;
        } else {
            throw new IllegalArgumentException("Invalid playlist position specified");
        }
    }

    @ResultName("track")
    public PlaylistItem setTrackMetadata(TrackMetadataRequest request) {
        if (request.getPlaylistPos() != null) {
            if (request.getPlaylistPos() < playerStatus.getPlaylist().getItems().size()) {
                if (request.getTrack().getId().equals(playerStatus.getPlaylist().getItems().get(request.getPlaylistPos()).getId())) {
                    if (request.getReplace()) {
                        playerStatus.getPlaylist().getItems().set(request.getPlaylistPos(), request.getTrack());
                    } else {
                        PlaylistItem item = playerStatus.getPlaylist().getItems().get(request.getPlaylistPos());
                        if (request.getTrack().getImage() != null) {
                            item.setImage(request.getTrack().getImage());
                        }
                        if (request.getTrack().getText() != null) {
                            item.setText(request.getTrack().getText());
                        }
                        if (request.getTrack().getType() != null) {
                            item.setType(request.getTrack().getType());
                        }
                        if (request.getTrack().getStreamingRefs() != null) {
                            item.setStreamingRefs(request.getTrack().getStreamingRefs());
                        }
                        //TODO: Implement copying of item attributes
                    }
                    sendPlaylistChangedNotification();
                    return playerStatus.getPlaylist().getItems().get(request.getPlaylistPos());
                } else {
                    throw new RuntimeException("Specified track doesn't exist at the specified playlist position");
                }
            } else {
                throw new RuntimeException("Invalid playlist position");
            }
        } else {
            PlaylistItem response = null;
            for (int playlistPos = 0; playlistPos < playerStatus.getPlaylist().getItems().size(); playlistPos++) {
                PlaylistItem item = playerStatus.getPlaylist().getItems().get(playlistPos);
                if (request.getTrack().getId().equals(item.getId())) {
                    if (request.getReplace()) {
                        playerStatus.getPlaylist().getItems().set(playlistPos, request.getTrack());
                        response = request.getTrack();
                    } else {
                        if (request.getTrack().getImage() != null) {
                            item.setImage(request.getTrack().getImage());
                        }
                        if (request.getTrack().getText() != null) {
                            item.setText(request.getTrack().getText());
                        }
                        if (request.getTrack().getType() != null) {
                            item.setType(request.getTrack().getType());
                        }
                        if (request.getTrack().getStreamingRefs() != null) {
                            item.setStreamingRefs(request.getTrack().getStreamingRefs());
                        }
                        //TODO: Implement copying of item attributes
                        response = item;
                    }
                    sendPlaylistChangedNotification();
                }
            }
            return response;
        }
    }

    public VolumeResponse getVolume() {
        VolumeResponse response = new VolumeResponse();
        if (player != null && !playerStatus.getMuted()) {
            response.setVolumeLevel(player.getVolume());
        } else {
            response.setVolumeLevel(playerStatus.getVolumeLevel());
        }
        response.setMuted(playerStatus.getMuted());
        return response;
    }

    public VolumeResponse setVolume(VolumeRequest request) {
        Double volume = playerStatus.getVolumeLevel();
        if (request.getVolumeLevel() != null) {
            volume = request.getVolumeLevel();
        } else if (request.getRelativeVolumeLevel() != null) {
            volume += request.getRelativeVolumeLevel();
        }
        if (volume < 0) {
            volume = 0d;
        }
        if (volume > 1) {
            volume = 1d;
        }
        playerStatus.setVolumeLevel(volume);
        if (player != null) {
            if ((request.getMuted() == null && !playerStatus.getMuted()) ||
                    (request.getMuted() != null && !request.getMuted())) {

                player.setVolume(volume);
            }
        }
        if (request.getMuted() != null) {
            playerStatus.setMuted(request.getMuted());
            if (player != null && request.getMuted()) {
                player.setVolume(0.0);
            }
        }

        return getVolume();
    }

    private void sendPlaylistChangedNotification() {
        if(notificationSender != null) {
            if(playerStatus.getPlaylist() != null) {
                notificationSender.playlistChanged(new PlaylistChangedNotification(playerStatus.getPlaylist().getId(), playerStatus.getPlaylist().getName(),playerStatus.getPlaylist().getItems().size()));
            }else {
                notificationSender.playlistChanged(new PlaylistChangedNotification(playerStatus.getPlaylist().getItems().size()));
            }
        }
    }

    private void sendPlayerStatusChangedNotification() {
        if(notificationSender != null) {
            PlayerStatusResponse notification = new PlayerStatusResponse();
            notification.setPlaying(playerStatus.getPlaying());
            notification.setPlaylistId(playerStatus.getPlaylist().getId());
            notification.setPlaylistName(playerStatus.getPlaylist().getName());
            notification.setPlaylistPos(playerStatus.getPlaylistPos());
            if (playerStatus.getPlaylistPos() != null && playerStatus.getPlaying()) {
                if(player != null) {
                    playerStatus.setSeekPos(player.getSeekPosition());
                }
            }
            notification.setSeekPos(playerStatus.getSeekPos());
            if (playerStatus.getPlaylist().getItems().size() > 0) {
                notification.setTrack(playerStatus.getPlaylist().getItems().get(playerStatus.getPlaylistPos()));
            }
            notificationSender.playerStatusChanged(notification);
        }
    }
}
