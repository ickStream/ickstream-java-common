/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.service.ImageReference;

import java.util.ArrayList;
import java.util.List;

public class PreferredMenuItem extends AbstractPreferredMenu {
    private String text;
    private String type;
    private List<ImageReference> images = new ArrayList<ImageReference>();

    public PreferredMenuItem() {
    }

    public PreferredMenuItem(String type, String text, List<ImageReference> images) {
        this.type = type;
        this.text = text;
        this.images = images;
    }

    public PreferredMenuItem(String type, String text, List<ImageReference> images, List<PreferredMenuItem> childItems) {
        super(childItems);
        this.type = type;
        this.text = text;
        this.images = images;
    }


    public PreferredMenuItem(String type, String text, List<ImageReference> images, PreferredMenuRequest childRequest) {
        super(childRequest);
        this.type = type;
        this.text = text;
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ImageReference> getImages() {
        return images;
    }

    public void setImages(List<ImageReference> images) {
        this.images = images;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
