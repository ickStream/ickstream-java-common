/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.protocol.ChunkedResponse;

import java.util.List;

public class FindDevicesResponse extends ChunkedResponse {
    private List<DeviceResponse> items_loop;

    public List<DeviceResponse> getItems_loop() {
        return items_loop;
    }

    public void setItems_loop(List<DeviceResponse> items_loop) {
        this.items_loop = items_loop;
    }
}
