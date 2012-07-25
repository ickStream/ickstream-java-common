package com.ickstream.controller.browse;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class BrowseResponse extends ChunkedResponse {
    private Long expirationTimestamp;
    private List<MenuItem> items_loop;

    public Long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(Long expirationTimestamp) {
        this.expirationTimestamp = expirationTimestamp;
    }

    public List<MenuItem> getItems_loop() {
        return items_loop;
    }

    public void setItems_loop(List<MenuItem> items_loop) {
        this.items_loop = items_loop;
    }
}
