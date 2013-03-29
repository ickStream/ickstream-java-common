/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import com.ickstream.protocol.common.data.StreamingReference;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import com.fasterxml.jackson.databind.JsonNode;

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

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlaylistItem)) {
            return false;
        }
        return new EqualsBuilder().append(id, ((PlaylistItem) o).getId()).isEquals();
    }
}
