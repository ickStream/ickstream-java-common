/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.service;

import com.ickstream.common.jsonrpc.JsonRpcParam;
import com.ickstream.common.jsonrpc.JsonRpcParamStructure;
import com.ickstream.common.jsonrpc.JsonRpcResult;
import com.ickstream.player.model.PlayerStatus;
import com.ickstream.protocol.common.ChunkedRequest;
import com.ickstream.protocol.service.player.*;

import java.util.*;

public class PlayerCommandService {
    private PlayerStatus playerStatus;
    private PlayerManager player;
    private Timer volumeNotificationTimer;

    public PlayerCommandService(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    public PlayerCommandService(PlayerManager player, PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
        this.player = player;
    }

    public synchronized PlayerConfigurationResponse setPlayerConfiguration(@JsonRpcParamStructure PlayerConfigurationRequest configuration) {
        if (configuration.getAccessToken() != null) {
            player.setAccessToken(configuration.getAccessToken());
        }
        if (configuration.getPlayerName() != null) {
            player.setName(configuration.getPlayerName());
        }
        return getPlayerConfiguration();
    }

    public synchronized PlayerConfigurationResponse getPlayerConfiguration() {
        PlayerConfigurationResponse response = new PlayerConfigurationResponse();
        response.setPlayerName(player.getName());
        response.setHardwareId(player.getHardwareId());
        response.setPlayerModel(player.getModel());
        return response;
    }

    public synchronized PlayerStatusResponse getPlayerStatus() {
        PlayerStatusResponse response = new PlayerStatusResponse();
        response.setPlaying(playerStatus.getPlaying());
        response.setPlaylistPos(playerStatus.getPlaylistPos());
        if (player != null && playerStatus.getPlaylistPos() != null && playerStatus.getPlaying()) {
            playerStatus.setSeekPos(player.getSeekPosition());
        }
        response.setSeekPos(playerStatus.getSeekPos());
        if (playerStatus.getPlaylist().getItems().size() > 0) {
            response.setTrack(playerStatus.getPlaylist().getItems().get(playerStatus.getPlaylistPos()));
        }
        if (player != null && !playerStatus.getMuted()) {
            response.setVolumeLevel(player.getVolume());
        } else {
            response.setVolumeLevel(playerStatus.getVolumeLevel());
        }
        response.setLastChanged(playerStatus.getChangedTimestamp());
        response.setMuted(playerStatus.getMuted());
        response.setRepeatMode(playerStatus.getRepeatMode());
        return response;
    }

    public synchronized SetPlaylistNameResponse setPlaylistName(@JsonRpcParamStructure SetPlaylistNameRequest request) {
        playerStatus.getPlaylist().setId(request.getPlaylistId());
        playerStatus.getPlaylist().setName(request.getPlaylistName());
        if (player != null) {
            player.sendPlaylistChangedNotification();
        }
        return new SetPlaylistNameResponse(playerStatus.getPlaylist().getId(), playerStatus.getPlaylist().getName(), playerStatus.getPlaylist().getItems().size());
    }

    public synchronized PlaylistResponse getPlaylist(@JsonRpcParamStructure ChunkedRequest request) {
        PlaylistResponse response = new PlaylistResponse();
        response.setPlaylistId(playerStatus.getPlaylist().getId());
        response.setPlaylistName(playerStatus.getPlaylist().getName());
        Integer offset = request.getOffset() != null ? request.getOffset() : 0;
        Integer count = request.getCount() != null ? request.getCount() : playerStatus.getPlaylist().getItems().size();
        response.setOffset(offset);
        response.setCountAll(playerStatus.getPlaylist().getItems().size());
        if (offset < playerStatus.getPlaylist().getItems().size()) {
            if (offset + count > playerStatus.getPlaylist().getItems().size()) {
                response.setItems(playerStatus.getPlaylist().getItems().subList(offset, playerStatus.getPlaylist().getItems().size()));
            } else {
                response.setItems(playerStatus.getPlaylist().getItems().subList(offset, offset + count));
            }
        } else {
            response.setItems(playerStatus.getPlaylist().getItems().subList(offset, playerStatus.getPlaylist().getItems().size()));
        }
        response.setCount(response.getItems().size());
        response.setLastChanged(playerStatus.getPlaylist().getChangedTimestamp());
        return response;
    }

    public synchronized PlaylistModificationResponse addTracks(@JsonRpcParamStructure PlaylistAddTracksRequest request) {
        if (request.getPlaylistPos() != null) {
            // Insert tracks in middle
            playerStatus.getPlaylist().getItems().addAll(request.getPlaylistPos(), request.getItems());
            playerStatus.getPlaylist().updateTimestamp();
            if (playerStatus.getPlaylistPos() != null && playerStatus.getPlaylistPos() >= request.getPlaylistPos()) {
                playerStatus.setPlaylistPos(playerStatus.getPlaylistPos() + request.getItems().size());
            }
        } else {
            // Add tracks at end
            playerStatus.getPlaylist().getItems().addAll(request.getItems());
            playerStatus.getPlaylist().updateTimestamp();
        }
        // Set playlist position to first track if there weren't any tracks in the playlist before
        if (playerStatus.getPlaylistPos() == null) {
            playerStatus.setPlaylistPos(0);
        }
        if (player != null) {
            player.sendPlaylistChangedNotification();
        }
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }

    public synchronized PlaylistModificationResponse removeTracks(@JsonRpcParamStructure PlaylistRemoveTracksRequest request) {

        List<PlaylistItem> modifiedPlaylist = new ArrayList<PlaylistItem>(playerStatus.getPlaylist().getItems());
        int modifiedPlaylistPos = playerStatus.getPlaylistPos();
        boolean affectsPlayback = false;
        for (PlaylistItemReference itemReference : request.getItems()) {
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
        if (!playerStatus.getPlaylistPos().equals(modifiedPlaylistPos)) {
            playerStatus.setPlaylistPos(modifiedPlaylistPos);
            if (playerStatus.getPlaying() && !affectsPlayback) {
                if (player != null) {
                    player.sendPlayerStatusChangedNotification();
                }
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
        if (player != null) {
            player.sendPlaylistChangedNotification();
        }
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }

    public synchronized PlaylistModificationResponse moveTracks(@JsonRpcParamStructure PlaylistMoveTracksRequest request) {
        Integer modifiedPlaylistPos = playerStatus.getPlaylistPos();
        List<PlaylistItem> modifiedPlaylist = new ArrayList<PlaylistItem>(playerStatus.getPlaylist().getItems());
        Integer wantedPlaylistPos = request.getPlaylistPos() != null ? request.getPlaylistPos() : playerStatus.getPlaylist().getItems().size();
        for (PlaylistItemReference playlistItemReference : request.getItems()) {
            if (playlistItemReference.getPlaylistPos() == null) {
                throw new IllegalArgumentException("moveTracks with items without playlistPos not supported");
            }
            if (playlistItemReference.getId() == null) {
                throw new IllegalArgumentException("moveTracks with items without id not supported");
            }
            // Move that doesn't affect playlist position
            if (wantedPlaylistPos <= modifiedPlaylistPos && playlistItemReference.getPlaylistPos() < modifiedPlaylistPos ||
                    wantedPlaylistPos > modifiedPlaylistPos && playlistItemReference.getPlaylistPos() > modifiedPlaylistPos) {

                PlaylistItem item = modifiedPlaylist.remove(playlistItemReference.getPlaylistPos().intValue());
                if (!item.getId().equals(playlistItemReference.getId())) {
                    throw new IllegalArgumentException("Playlist position " + playlistItemReference.getPlaylistPos() + " does not match " + playlistItemReference.getId());
                }
                int offset = 0;
                if (wantedPlaylistPos >= playlistItemReference.getPlaylistPos()) {
                    offset = -1;
                }
                if (wantedPlaylistPos + offset < modifiedPlaylist.size()) {
                    modifiedPlaylist.add(wantedPlaylistPos + offset, item);
                } else {
                    modifiedPlaylist.add(item);
                }
                if (wantedPlaylistPos < playlistItemReference.getPlaylistPos()) {
                    wantedPlaylistPos++;
                }

                // Move that increase playlist position
            } else if (wantedPlaylistPos <= modifiedPlaylistPos && playlistItemReference.getPlaylistPos() > modifiedPlaylistPos) {
                PlaylistItem item = modifiedPlaylist.remove(playlistItemReference.getPlaylistPos().intValue());
                if (!item.getId().equals(playlistItemReference.getId())) {
                    throw new IllegalArgumentException("Playlist position " + playlistItemReference.getPlaylistPos() + " does not match " + playlistItemReference.getId());
                }
                modifiedPlaylist.add(wantedPlaylistPos, item);
                modifiedPlaylistPos++;
                wantedPlaylistPos++;

                // Move that decrease playlist position
            } else if (wantedPlaylistPos > modifiedPlaylistPos && playlistItemReference.getPlaylistPos() < modifiedPlaylistPos) {
                PlaylistItem item = modifiedPlaylist.remove(playlistItemReference.getPlaylistPos().intValue());
                if (!item.getId().equals(playlistItemReference.getId())) {
                    throw new IllegalArgumentException("Playlist position " + playlistItemReference.getPlaylistPos() + " does not match " + playlistItemReference.getId());
                }
                int offset = 0;
                if (wantedPlaylistPos >= playlistItemReference.getPlaylistPos()) {
                    offset = -1;
                }
                if (wantedPlaylistPos + offset < modifiedPlaylist.size()) {
                    modifiedPlaylist.add(wantedPlaylistPos + offset, item);
                } else {
                    modifiedPlaylist.add(item);
                }
                modifiedPlaylistPos--;

                // Move of currently playing track
            } else if (playlistItemReference.getPlaylistPos().equals(modifiedPlaylistPos)) {
                PlaylistItem item = modifiedPlaylist.remove(playlistItemReference.getPlaylistPos().intValue());
                if (!item.getId().equals(playlistItemReference.getId())) {
                    throw new IllegalArgumentException("Playlist position " + playlistItemReference.getPlaylistPos() + " does not match " + playlistItemReference.getId());
                }
                if (wantedPlaylistPos < modifiedPlaylist.size() + 1) {
                    if (wantedPlaylistPos > playlistItemReference.getPlaylistPos()) {
                        modifiedPlaylist.add(wantedPlaylistPos - 1, item);
                        modifiedPlaylistPos = wantedPlaylistPos - 1;
                    } else {
                        modifiedPlaylist.add(wantedPlaylistPos, item);
                        modifiedPlaylistPos = wantedPlaylistPos;
                    }
                } else {
                    modifiedPlaylist.add(item);
                    modifiedPlaylistPos = wantedPlaylistPos - 1;
                }
                if (wantedPlaylistPos < playlistItemReference.getPlaylistPos()) {
                    wantedPlaylistPos++;
                }
            }
        }
        playerStatus.getPlaylist().setItems(modifiedPlaylist);
        playerStatus.getPlaylist().updateTimestamp();
        playerStatus.setPlaylistPos(modifiedPlaylistPos);
        return new PlaylistModificationResponse(true, modifiedPlaylistPos);
    }

    public synchronized PlaylistModificationResponse setTracks(@JsonRpcParamStructure PlaylistSetTracksRequest request) {
        playerStatus.getPlaylist().setId(request.getPlaylistId());
        playerStatus.getPlaylist().setName(request.getPlaylistName());
        playerStatus.getPlaylist().setItems(request.getItems());

        Integer playlistPos = request.getPlaylistPos() != null ? request.getPlaylistPos() : 0;
        if (request.getItems().size() > 0) {
            setTrack(playlistPos);
        } else {
            playerStatus.setSeekPos(null);
            playerStatus.setPlaylistPos(null);
            if (player != null && playerStatus.getPlaying()) {
                player.pause();
            }
        }
        if (player != null) {
            player.sendPlaylistChangedNotification();
        }
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }


    @JsonRpcResult("playing")
    public synchronized Boolean play(@JsonRpcParam(name = "playing") Boolean play) {
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

    public synchronized SeekPosition getSeekPosition() {
        SeekPosition response = new SeekPosition();
        response.setPlaylistPos(playerStatus.getPlaylistPos());
        if (player != null && playerStatus.getPlaylistPos() != null && playerStatus.getPlaying()) {
            playerStatus.setSeekPos(player.getSeekPosition());
        }
        response.setSeekPos(playerStatus.getSeekPos());
        return response;
    }

    public synchronized SeekPosition setSeekPosition(@JsonRpcParamStructure SeekPosition request) {
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

    public synchronized TrackResponse getTrack(@JsonRpcParam(name = "playlistPos", optional = true) Integer playlistPos) {
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

    @JsonRpcResult("playlistPos")
    public synchronized Integer setTrack(@JsonRpcParam(name = "playlistPos") Integer playlistPos) {
        if (playlistPos != null && playlistPos < playerStatus.getPlaylist().getItems().size()) {
            playerStatus.setPlaylistPos(playlistPos);
            playerStatus.setSeekPos(0d);
            // Make sure we make the player aware that it should change track
            if (playerStatus.getPlaying() && player != null) {
                player.play();
            } else {
                if (player != null) {
                    player.sendPlayerStatusChangedNotification();
                }
            }
            return playlistPos;
        } else {
            throw new IllegalArgumentException("Invalid playlist position specified");
        }
    }

    @JsonRpcResult("track")
    public synchronized PlaylistItem setTrackMetadata(@JsonRpcParamStructure TrackMetadataRequest request) {
        if (request.getPlaylistPos() != null) {
            if (request.getPlaylistPos() < playerStatus.getPlaylist().getItems().size()) {
                if (request.getTrack().getId().equals(playerStatus.getPlaylist().getItems().get(request.getPlaylistPos()).getId())) {
                    if (request.getReplace()) {
                        playerStatus.getPlaylist().getItems().set(request.getPlaylistPos(), request.getTrack());
                        playerStatus.getPlaylist().updateTimestamp();
                    } else {
                        PlaylistItem item = playerStatus.getPlaylist().getItems().get(request.getPlaylistPos());
                        if (request.getTrack().getImage() != null) {
                            item.setImage(request.getTrack().getImage());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        if (request.getTrack().getText() != null) {
                            item.setText(request.getTrack().getText());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        if (request.getTrack().getType() != null) {
                            item.setType(request.getTrack().getType());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        if (request.getTrack().getStreamingRefs() != null) {
                            item.setStreamingRefs(request.getTrack().getStreamingRefs());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        //TODO: Implement copying of item attributes
                    }
                    if (playerStatus.getPlaylistPos() != null && playerStatus.getPlaylistPos().equals(request.getPlaylistPos())) {
                        playerStatus.updateTimestamp();
                        if (player != null) {
                            player.sendPlayerStatusChangedNotification();
                        }
                    }
                    if (player != null) {
                        player.sendPlaylistChangedNotification();
                    }
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
                        playerStatus.getPlaylist().updateTimestamp();
                        response = request.getTrack();
                    } else {
                        if (request.getTrack().getImage() != null) {
                            item.setImage(request.getTrack().getImage());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        if (request.getTrack().getText() != null) {
                            item.setText(request.getTrack().getText());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        if (request.getTrack().getType() != null) {
                            item.setType(request.getTrack().getType());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        if (request.getTrack().getStreamingRefs() != null) {
                            item.setStreamingRefs(request.getTrack().getStreamingRefs());
                            playerStatus.getPlaylist().updateTimestamp();
                        }
                        //TODO: Implement copying of item attributes
                        response = item;
                    }
                    if (playerStatus.getPlaylistPos() != null && playerStatus.getPlaylistPos().equals(playlistPos)) {
                        playerStatus.updateTimestamp();
                        if (player != null) {
                            player.sendPlayerStatusChangedNotification();
                        }
                    }
                }
            }
            if (player != null) {
                player.sendPlaylistChangedNotification();
            }
            return response;
        }
    }

    public synchronized VolumeResponse getVolume() {
        VolumeResponse response = new VolumeResponse();
        if (player != null && !playerStatus.getMuted()) {
            response.setVolumeLevel(player.getVolume());
        } else {
            response.setVolumeLevel(playerStatus.getVolumeLevel());
        }
        response.setMuted(playerStatus.getMuted());
        return response;
    }

    public synchronized VolumeResponse setVolume(@JsonRpcParamStructure VolumeRequest request) {
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
        if (volumeNotificationTimer == null) {
            volumeNotificationTimer = new Timer();
            volumeNotificationTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (player != null) {
                        player.sendPlayerStatusChangedNotification();
                    }
                    volumeNotificationTimer = null;
                }
            }, 2000);
        }
        return getVolume();
    }

    public synchronized RepeatModeResponse setRepeatMode(@JsonRpcParamStructure RepeatModeRequest request) {
        playerStatus.setRepeatMode(request.getRepeatMode());
        if (player != null) {
            player.sendPlayerStatusChangedNotification();
        }
        return new RepeatModeResponse(playerStatus.getRepeatMode());
    }

    public synchronized PlaylistModificationResponse shuffleTracks() {
        List<PlaylistItem> playlistItems = playerStatus.getPlaylist().getItems();
        if (playlistItems.size() > 1) {
            PlaylistItem currentItem = null;
            if (playerStatus.getPlaylistPos() != null && playerStatus.getPlaylistPos() < playlistItems.size()) {
                currentItem = playlistItems.remove(playerStatus.getPlaylistPos().intValue());
            }
            Collections.shuffle(playlistItems);
            if (currentItem != null) {
                playlistItems.add(0, currentItem);
            }
            playerStatus.getPlaylist().setItems(playlistItems);
            playerStatus.setPlaylistPos(0);
            if (player != null) {
                player.sendPlaylistChangedNotification();
                player.sendPlayerStatusChangedNotification();
            }
        }
        return new PlaylistModificationResponse(true, playerStatus.getPlaylistPos());
    }
}
