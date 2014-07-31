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
    private String menuType;
    private String contextId;
    private List<ImageReference> images = new ArrayList<ImageReference>();

    public PreferredMenuItem() {
    }

    public PreferredMenuItem(String contextId, String type, String text, String menuType, List<ImageReference> images) {
        this.contextId = contextId;
        this.type = type;
        this.text = text;
        this.images = images;
        this.menuType = menuType;
    }

    public PreferredMenuItem(String contextId, String type, String text, String menuType, List<ImageReference> images, List<PreferredMenuItem> childItems) {
        super(childItems);
        this.contextId = contextId;
        this.type = type;
        this.text = text;
        this.images = images;
        this.menuType = menuType;
    }


    public PreferredMenuItem(String contextId, String type, String text, String menuType, List<ImageReference> images, PreferredMenuRequest childRequest) {
        super(childRequest);
        this.contextId = contextId;
        this.type = type;
        this.text = text;
        this.images = images;
        this.menuType = menuType;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
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

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
