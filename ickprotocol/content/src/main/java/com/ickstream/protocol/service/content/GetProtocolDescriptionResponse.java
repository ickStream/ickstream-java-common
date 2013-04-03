/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class GetProtocolDescriptionResponse extends ChunkedResponse {
    private List<ProtocolDescriptionContext> items = new ArrayList<ProtocolDescriptionContext>();

    public List<ProtocolDescriptionContext> getItems() {
        return items;
    }

    public void setItems(List<ProtocolDescriptionContext> items) {
        this.items = items;
    }
}
