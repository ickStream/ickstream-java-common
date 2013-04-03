/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

import com.ickstream.protocol.common.data.AbstractContentItem;
import com.ickstream.protocol.common.data.StreamingReference;
import com.ickstream.protocol.common.data.TrackAttributes;

import java.util.List;

public class PlaylistItem implements AbstractContentItem {
    private String id;
    private String parentNode;
    private String text;
    private String type;
    private String image;
    private List<StreamingReference> streamingRefs;
    private TrackAttributes itemAttributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
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

    public TrackAttributes getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(TrackAttributes itemAttributes) {
        this.itemAttributes = itemAttributes;
    }
}
