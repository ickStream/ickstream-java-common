/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.common.data;

public class StreamingReference {
    private String format;
    private String url;
    private Boolean intermediate;

    public StreamingReference() {
    }

    public StreamingReference(String format, String url) {
        this.url = url;
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIntermediate() {
        return intermediate;
    }

    public void setIntermediate(Boolean intermediate) {
        this.intermediate = intermediate;
    }
}
