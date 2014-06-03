/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedRequest;

public class GetPreferredMenusRequest extends ChunkedRequest {
    private String language;

    public GetPreferredMenusRequest() {
    }

    public GetPreferredMenusRequest(String language) {
        this.language = language;
    }

    public GetPreferredMenusRequest(Integer offset, Integer count) {
        super(offset, count);
    }

    public GetPreferredMenusRequest(Integer offset, Integer count, String language) {
        super(offset, count);
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
