/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service;

import java.io.Serializable;

public class ImageReference implements Serializable {
    private String url;
    private String type;
    private Integer width;
    private Integer height;

    public ImageReference() {
    }

    public ImageReference(String url, String type, Integer width, Integer height) {
        this.url = url;
        this.type = type;
        this.width = width;
        this.height = height;
    }

    public ImageReference(String url, ImageType type) {
        this.url = url;
        this.type = type.getName();
        this.width = type.getWidth();
        this.height = type.getHeight();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
