/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.core;

import com.ickstream.protocol.ChunkedRequest;

public class FindServicesRequest extends ChunkedRequest {
    private String type;

    public FindServicesRequest() {
    }

    public FindServicesRequest(String type) {
        this.type = type;
    }

    public FindServicesRequest(Integer offset, Integer count) {
        super(offset, count);
    }

    public FindServicesRequest(Integer offset, Integer count, String type) {
        super(offset, count);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
