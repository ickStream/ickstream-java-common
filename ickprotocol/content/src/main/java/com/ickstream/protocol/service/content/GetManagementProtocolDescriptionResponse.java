/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class GetManagementProtocolDescriptionResponse extends ChunkedResponse {
    private List<ManagementProtocolDescriptionContext> items = new ArrayList<ManagementProtocolDescriptionContext>();

    public List<ManagementProtocolDescriptionContext> getItems() {
        return items;
    }

    public void setItems(List<ManagementProtocolDescriptionContext> items) {
        this.items = items;
    }
}
