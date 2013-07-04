/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ickstream.protocol.service.player.PlaybackQueueItem;
import com.ickstream.protocol.service.player.PlaybackQueueMode;

public class PlayerStatus {
    private Long changedTimestamp = System.currentTimeMillis();
    private Boolean playing = Boolean.FALSE;
    private Double volumeLevel = 0.1;
    private Boolean muted = Boolean.FALSE;
    private Double seekPos;
    private Integer playbackQueuePos;
    private PlaybackQueueMode playbackQueueMode = PlaybackQueueMode.QUEUE;
    @JsonIgnore
    private PlaybackQueue playbackQueue;

    @JsonIgnore
    private PlayerStatusStorage storage;

    public PlayerStatus() {
        this(new PlaybackQueue());
    }

    public PlayerStatus(PlaybackQueue playbackQueue) {
        this.playbackQueue = playbackQueue;
    }

    public PlayerStatus(PlaybackQueue playbackQueue, PlayerStatusStorage storage) {
        this(playbackQueue);
        this.storage = storage;
    }

    public void updateTimestamp() {
        Long newTimestamp = System.currentTimeMillis();
        if (newTimestamp.equals(changedTimestamp)) {
            newTimestamp++;
        }
        changedTimestamp = newTimestamp;
        if (this.storage != null) {
            storage.store(this);
        }
    }

    public Boolean getPlaying() {
        return playing;
    }

    public void setPlaying(Boolean playing) {
        this.playing = playing;
        updateTimestamp();
    }

    public Double getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(Double volumeLevel) {
        this.volumeLevel = volumeLevel;
        updateTimestamp();
    }

    public Boolean getMuted() {
        return muted;
    }

    public void setMuted(Boolean muted) {
        this.muted = muted;
        updateTimestamp();
    }

    public Double getSeekPos() {
        return seekPos;
    }

    public void setSeekPos(Double seekPos) {
        this.seekPos = seekPos;
    }

    public Integer getPlaybackQueuePos() {
        return playbackQueuePos;
    }

    public void setPlaybackQueuePos(Integer playbackQueuePos) {
        if ((playbackQueuePos == null && this.playbackQueuePos != null) || (playbackQueuePos != null && !playbackQueuePos.equals(this.playbackQueuePos))) {
            this.playbackQueuePos = playbackQueuePos;
            updateTimestamp();
        }
    }

    public PlaybackQueue getPlaybackQueue() {
        return playbackQueue;
    }

    public void setPlaybackQueue(PlaybackQueue playbackQueue) {
        if (playbackQueue != null) {
            this.playbackQueue = playbackQueue;
        } else {
            this.playbackQueue = new PlaybackQueue();
        }
        updateTimestamp();
    }

    public PlaybackQueueMode getPlaybackQueueMode() {
        return playbackQueueMode;
    }

    public void setPlaybackQueueMode(PlaybackQueueMode playbackQueueMode) {
        if (!this.playbackQueueMode.equals(playbackQueueMode)) {
            this.playbackQueueMode = playbackQueueMode;
            updateTimestamp();
        }
    }

    @JsonIgnore
    public PlaybackQueueItem getCurrentPlaylistItem() {
        if (getPlaybackQueuePos() != null) {
            if (getPlaybackQueuePos() < getPlaybackQueue().getItems().size()) {
                return getPlaybackQueue().getItems().get(getPlaybackQueuePos());
            }
        }
        return null;
    }

    public Long getChangedTimestamp() {
        return changedTimestamp;
    }

    public PlayerStatusStorage getStorage() {
        return storage;
    }

    public void setStorage(PlayerStatusStorage storage) {
        this.storage = storage;
    }
}
