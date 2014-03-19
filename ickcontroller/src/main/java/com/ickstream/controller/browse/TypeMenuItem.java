/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;

import java.util.List;

public class TypeMenuItem extends AbstractMenuItem {
    public final static String TYPE = "type";
    private String id;
    private String text;
    private String image;

    public TypeMenuItem(String id, String text, String image) {
        this(null, null, id, text, image, null);
    }

    public TypeMenuItem(ServiceController serviceController, String contextId, String id, String text, String image) {
        this(serviceController, contextId, id, text, image, null);
    }

    public TypeMenuItem(ServiceController serviceController, String contextId, String id, String text, String image, MenuItem parent) {
        super(serviceController, contextId, parent);
        this.id = id;
        this.text = text;
        this.image = image;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public List<String> getPreferredChildItems() {
        return null;
    }
}
