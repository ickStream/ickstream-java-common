/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service;

public class ImageType {
    private String name;
    private Integer height;
    private Integer width;

    public static final ImageType SMALL = new ImageType("small", 80, 50);

    public ImageType(Integer width, Integer height) {
        this.name = "custom";
        this.height = height;
        this.width = width;
    }

    private ImageType(String name, Integer width, Integer height) {
        this.name = name;
        this.height = height;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }
}
