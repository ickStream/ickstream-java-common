/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.core;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.List;

public class FindAuthenticationProviderResponse extends ChunkedResponse {
    private List<AuthenticationProviderResponse> items;

    public List<AuthenticationProviderResponse> getItems() {
        return items;
    }

    public void setItems(List<AuthenticationProviderResponse> items) {
        this.items = items;
    }
}
