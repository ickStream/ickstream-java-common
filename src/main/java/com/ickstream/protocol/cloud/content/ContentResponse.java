/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.protocol.ChunkedResponse;

import java.util.List;

public class ContentResponse extends ChunkedResponse {
    private Long expirationTimestamp;
    private List<ContentItem> items_loop;

    public List<ContentItem> getItems_loop() {
        return items_loop;
    }

    public void setItems_loop(List<ContentItem> items_loop) {
        this.items_loop = items_loop;
    }

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}
