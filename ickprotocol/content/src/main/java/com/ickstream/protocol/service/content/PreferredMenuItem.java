/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.List;

public class PreferredMenuItem extends AbstractPreferredMenu {
    private String text;
    private String type;
    private String image;

    public PreferredMenuItem() {
    }

    public PreferredMenuItem(String type, String text, String image) {
        this.type = type;
        this.text = text;
        this.image = image;
    }

    public PreferredMenuItem(String type, String text, String image, List<PreferredMenuItem> childItems) {
        super(childItems);
        this.type = type;
        this.text = text;
        this.image = image;
    }


    public PreferredMenuItem(String type, String text, String image, PreferredMenuRequest childRequest) {
        super(childRequest);
        this.type = type;
        this.text = text;
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
