/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
