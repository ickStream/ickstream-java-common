/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;
import com.ickstream.protocol.common.data.ContentItem;

import java.util.List;

public class ContentResponse extends ChunkedResponse {
    private Long expirationTimestamp;
    private List<ContentItem> items;

    public List<ContentItem> getItems() {
        return items;
    }

    public void setItems(List<ContentItem> items) {
        this.items = items;
    }

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }
}
