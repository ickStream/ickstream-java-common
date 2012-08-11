/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class ProtocolDescriptionResponse extends ChunkedResponse {
    private List<ProtocolDescriptionResponseContext> items;

    public List<ProtocolDescriptionResponseContext> getItems() {
        return items;
    }

    public void setItems(List<ProtocolDescriptionResponseContext> items) {
        this.items = items;
    }
}
