/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.fasterxml.jackson.databind.JsonNode;

public class AccountChangeResponse {
    private String id;
    private Long occurrenceTimestamp;
    private String type;
    private String text;
    private JsonNode itemAttributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getOccurrenceTimestamp() {
        return occurrenceTimestamp;
    }

    public void setOccurrenceTimestamp(Long occurrenceTimestamp) {
        this.occurrenceTimestamp = occurrenceTimestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public JsonNode getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(JsonNode itemAttributes) {
        this.itemAttributes = itemAttributes;
    }
}
