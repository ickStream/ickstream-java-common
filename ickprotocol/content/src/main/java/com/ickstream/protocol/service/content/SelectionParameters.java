/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.fasterxml.jackson.databind.JsonNode;

public class SelectionParameters {
    public static final String TYPE_RANDOM_ALL = "RANDOM_ALL";
    public static final String TYPE_RANDOM_MY_LIBRARY = "RANDOM_MY_LIBRARY";
    public static final String TYPE_RANDOM_MY_PLAYLISTS = "RANDOM_MY_PLAYLISTS";
    public static final String TYPE_RANDOM_FRIENDS = "RANDOM_FRIENDS";
    public static final String TYPE_RANDOM_FOR_ARTIST = "RANDOM_FOR_ARTIST";
    public static final String TYPE_RANDOM_FOR_PLAYLIST = "RANDOM_FOR_PLAYLIST";
    public static final String TYPE_RANDOM_FOR_CATEGORY = "RANDOM_FOR_CATEGORY";

    private String type;
    private JsonNode data;

    public SelectionParameters() {
    }

    public SelectionParameters(String type) {
        this.type = type;
    }

    public SelectionParameters(String type, JsonNode data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
