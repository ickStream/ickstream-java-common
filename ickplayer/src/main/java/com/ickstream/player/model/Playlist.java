/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ickstream.protocol.service.player.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private Long changedTimestamp = System.currentTimeMillis();
    private String id;
    private String name;
    private List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    @JsonIgnore
    private PlaylistStorage storage;

    public Playlist() {
    }

    public Playlist(PlaylistStorage storage) {
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

    public List<PlaylistItem> getItems() {
        return items;
    }

    public void setItems(List<PlaylistItem> items) {
        this.items = items;
        updateTimestamp();
    }

    public Long getChangedTimestamp() {
        return changedTimestamp;
    }

    public PlaylistStorage getStorage() {
        return storage;
    }

    public void setStorage(PlaylistStorage storage) {
        this.storage = storage;
    }
}
