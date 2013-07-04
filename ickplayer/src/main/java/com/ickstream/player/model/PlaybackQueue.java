/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ickstream.protocol.service.player.PlaybackQueueItem;

import java.util.ArrayList;
import java.util.List;

public class PlaybackQueue {
    private Long changedTimestamp = System.currentTimeMillis();
    private String id;
    private String name;
    private List<PlaybackQueueItem> items = new ArrayList<PlaybackQueueItem>();

    @JsonIgnore
    private PlaybackQueueStorage storage;

    public PlaybackQueue() {
    }

    public PlaybackQueue(PlaybackQueueStorage storage) {
        this.storage = storage;
    }

    public void updateTimestamp() {
        Long newTimestamp = System.currentTimeMillis();
        if (newTimestamp.equals(changedTimestamp)) {
            newTimestamp++;
        }
        changedTimestamp = newTimestamp;
        if (storage != null) {
            storage.store(this);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        updateTimestamp();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateTimestamp();
    }

    public List<PlaybackQueueItem> getItems() {
        return items;
    }

    public void setItems(List<PlaybackQueueItem> items) {
        this.items = items;
        updateTimestamp();
    }

    public Long getChangedTimestamp() {
        return changedTimestamp;
    }

    public PlaybackQueueStorage getStorage() {
        return storage;
    }

    public void setStorage(PlaybackQueueStorage storage) {
        this.storage = storage;
    }
}
