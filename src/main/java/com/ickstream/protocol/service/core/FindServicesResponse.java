/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class FindServicesResponse extends ChunkedResponse {
    private List<ServiceResponse> items;

    public List<ServiceResponse> getItems() {
        return items;
    }

    public void setItems(List<ServiceResponse> items) {
        this.items = items;
    }
}
