/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.ChunkedRequest;

public class GetProtocolDescriptionRequest extends ChunkedRequest {
    private String language;

    public GetProtocolDescriptionRequest() {
    }

    public GetProtocolDescriptionRequest(Integer offset, Integer count) {
        super(offset, count);
    }

    public GetProtocolDescriptionRequest(String language) {
        this.language = language;
    }

    public GetProtocolDescriptionRequest(Integer offset, Integer count, String language) {
        super(offset, count);
        this.language = language;
    }
}
