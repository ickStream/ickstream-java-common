/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class ProtocolDescriptionResponse extends ChunkedResponse {
    private List<ProtocolDescriptionResponseContext> items_loop;

    public List<ProtocolDescriptionResponseContext> getItems_loop() {
        return items_loop;
    }

    public void setItems_loop(List<ProtocolDescriptionResponseContext> items_loop) {
        this.items_loop = items_loop;
    }
}
