/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.model;

import com.ickstream.protocol.device.player.PlaylistItem;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private Long changedTimestamp = System.currentTimeMillis();
    String id;
    String name;
    List<PlaylistItem> items = new ArrayList<PlaylistItem>();

    public void updateTimestamp() {
        Long newTimestamp = System.currentTimeMillis();
        if (newTimestamp.equals(changedTimestamp)) {
            newTimestamp++;
        }
        changedTimestamp = newTimestamp;
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
}
