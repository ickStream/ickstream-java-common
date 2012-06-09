/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device.player;

import com.ickstream.protocol.StreamingReference;
import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class PlaylistItem {
    private String id;
    private String text;
    private String type;
    private String image;
    private List<StreamingReference> streamingRefs = new ArrayList<StreamingReference>();
    private JsonNode itemAttributes;

    public PlaylistItem() {
    }

    public PlaylistItem(String id, String text, String type, String image) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.image = image;
    }

    public PlaylistItem(String id, String text, String type, String image, List<StreamingReference> streamingRefs) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.image = image;
        this.streamingRefs = streamingRefs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<StreamingReference> getStreamingRefs() {
        return streamingRefs;
    }

    public void setStreamingRefs(List<StreamingReference> streamingRefs) {
        this.streamingRefs = streamingRefs;
    }

    public JsonNode getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(JsonNode itemAttributes) {
        this.itemAttributes = itemAttributes;
    }
}
