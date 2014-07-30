/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service;

public class ImageType {
    private String name;
    private Integer height;
    private Integer width;

    public static final ImageType RIBBON = new ImageType("ribbon", 54, 106);
    public static final ImageType ICON_RGB = new ImageType("icon_rgb", 300, 300);
    public static final ImageType ICON_BW = new ImageType("icon_bw", 80, 60);
    public static final ImageType IMAGE = new ImageType("image", null, null);


    public ImageType(Integer width, Integer height) {
        this.name = IMAGE.getName();
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
