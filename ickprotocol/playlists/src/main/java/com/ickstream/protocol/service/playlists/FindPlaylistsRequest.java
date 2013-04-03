/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.playlists;

import com.ickstream.protocol.common.ChunkedRequest;

public class FindPlaylistsRequest extends ChunkedRequest {
    private String search;

    public FindPlaylistsRequest() {
    }

    public FindPlaylistsRequest(Integer offset, Integer count) {
        super(offset, count);
    }

    public FindPlaylistsRequest(String search) {
        this.search = search;
    }

    public FindPlaylistsRequest(Integer offset, Integer count, String search) {
        super(offset, count);
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
