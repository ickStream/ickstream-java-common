/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common.data;

public class StreamingReference {
    private String format;
    private String url;
    private Boolean intermediate;
    private Integer sampleRate;
    private Integer sampleSize;
    private Integer channels;
    private String streamFormatInformation;

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

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Integer getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(Integer sampleSize) {
        this.sampleSize = sampleSize;
    }

    public Integer getChannels() {
        return channels;
    }

    public void setChannels(Integer channels) {
        this.channels = channels;
    }

    public String getStreamFormatInformation() {
        return streamFormatInformation;
    }

    public void setStreamFormatInformation(String streamFormatInformation) {
        this.streamFormatInformation = streamFormatInformation;
    }
}
