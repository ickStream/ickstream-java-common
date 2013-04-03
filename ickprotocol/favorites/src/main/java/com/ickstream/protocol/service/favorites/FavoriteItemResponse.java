/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.favorites;


import com.ickstream.protocol.common.ChunkedResponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteItemResponse extends ChunkedResponse {
    private List<FavoriteItem> items = new ArrayList<FavoriteItem>();

    public List<FavoriteItem> getItems() {
        return items;
    }

    public void setItems(List<FavoriteItem> items) {
        this.items = items;
    }
}
