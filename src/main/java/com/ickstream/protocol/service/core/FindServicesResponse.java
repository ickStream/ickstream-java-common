/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.core;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class FindServicesResponse extends ChunkedResponse {
    private List<ServiceResponse> items_loop;

    public List<ServiceResponse> getItems_loop() {
        return items_loop;
    }

    public void setItems_loop(List<ServiceResponse> items_loop) {
        this.items_loop = items_loop;
    }
}
