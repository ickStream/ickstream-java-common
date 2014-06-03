/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class GetProtocolDescription2Response extends ChunkedResponse {
    private List<ProtocolDescription2Context> items = new ArrayList<ProtocolDescription2Context>();

    public List<ProtocolDescription2Context> getItems() {
        return items;
    }

    public void setItems(List<ProtocolDescription2Context> items) {
        this.items = items;
    }
}
