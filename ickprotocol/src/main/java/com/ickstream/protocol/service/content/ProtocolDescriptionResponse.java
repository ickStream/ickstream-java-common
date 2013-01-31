/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
