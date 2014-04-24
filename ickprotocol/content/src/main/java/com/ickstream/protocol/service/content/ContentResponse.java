/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;
import com.ickstream.protocol.common.data.ContentItem;

import java.util.ArrayList;
import java.util.List;

public class ContentResponse extends ChunkedResponse {
    private Long expirationTimestamp;
    private Long lastChanged;
    private List<ContentItem> items = new ArrayList<ContentItem>();

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

    public Long getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Long lastChanged) {
        if (lastChanged != null && lastChanged > 2147483647) {
            this.lastChanged = lastChanged / 1000;
        } else {
            this.lastChanged = lastChanged;
        }
    }
}
