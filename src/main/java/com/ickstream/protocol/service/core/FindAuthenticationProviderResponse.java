/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
