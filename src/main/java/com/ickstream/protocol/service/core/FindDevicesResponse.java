/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.core;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class FindDevicesResponse extends ChunkedResponse {
    private List<DeviceResponse> items;

    public List<DeviceResponse> getItems() {
        return items;
    }

    public void setItems(List<DeviceResponse> items) {
        this.items = items;
    }
}
