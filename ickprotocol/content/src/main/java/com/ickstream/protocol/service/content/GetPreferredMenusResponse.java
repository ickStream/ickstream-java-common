/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class GetPreferredMenusResponse extends ChunkedResponse {
    private List<PreferredMenuItem> items = new ArrayList<PreferredMenuItem>();

    public List<PreferredMenuItem> getItems() {
        return items;
    }

    public void setItems(List<PreferredMenuItem> items) {
        this.items = items;
    }
}
