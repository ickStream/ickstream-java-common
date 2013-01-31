/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common;

public class ChunkedRequest {
    private Integer offset;
    private Integer count;

    public ChunkedRequest() {
    }

    public ChunkedRequest(Integer offset, Integer count) {
        this.offset = offset;
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
