/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common.data;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class ContentItem implements AbstractContentItem {
    private String id;
    private String parentNode;
    private String text;
    private String sortText;
    private String type;
    private String image;
    private List<StreamingReference> streamingRefs;
    private List<String> preferredChildItems;
    private String preferredChildRequest;
    private JsonNode itemAttributes;

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

    public String getSortText() {
        return sortText;
    }

    public void setSortText(String sortText) {
        this.sortText = sortText;
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

    public List<String> getPreferredChildItems() {
        return preferredChildItems;
    }

    public void setPreferredChildItems(List<String> preferredChildItems) {
        this.preferredChildItems = preferredChildItems;
    }

    public String getPreferredChildRequest() {
        return preferredChildRequest;
    }

    public void setPreferredChildRequest(String preferredChildRequest) {
        this.preferredChildRequest = preferredChildRequest;
    }

    public JsonNode getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(JsonNode itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    public String getParentNode() {
        return parentNode;
    }

    public void setParentNode(String parentNode) {
        this.parentNode = parentNode;
    }
}
