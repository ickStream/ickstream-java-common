package com.ickstream.controller.browse;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class BrowseResponse extends ChunkedResponse {
    private Long expirationTimestamp;
    private List<MenuItem> items;

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
}
