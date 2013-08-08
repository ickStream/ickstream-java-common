/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class FindAccountChangesResponse extends ChunkedResponse {
    private List<AccountChangeResponse> items;

    public List<AccountChangeResponse> getItems() {
        return items;
    }

    public void setItems(List<AccountChangeResponse> items) {
        this.items = items;
    }
}
