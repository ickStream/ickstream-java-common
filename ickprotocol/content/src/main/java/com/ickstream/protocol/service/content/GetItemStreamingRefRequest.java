/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.ArrayList;
import java.util.List;

public class GetItemStreamingRefRequest {
    private String itemId;
    private List<String> preferredFormats = new ArrayList<String>();

    public GetItemStreamingRefRequest() {
    }

    public GetItemStreamingRefRequest(String itemId) {
        this.itemId = itemId;
    }

    public GetItemStreamingRefRequest(String itemId, List<String> preferredFormats) {
        this.itemId = itemId;
        this.preferredFormats = preferredFormats;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public List<String> getPreferredFormats() {
        return preferredFormats;
    }

    public void setPreferredFormats(List<String> preferredFormats) {
        this.preferredFormats = preferredFormats;
    }
}
