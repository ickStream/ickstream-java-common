/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;

public class ContextMenuItem extends AbstractMenuItem {
    public final static String TYPE = "context";
    private String text;
    private String image;

    public ContextMenuItem(ServiceController serviceController, String contextId, String text, String image) {
        super(serviceController, contextId, null);
        this.text = text;
        this.image = image;
    }

    @Override
    public String getType() {
        return TYPE;
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
    public String getId() {
        return getContextId();
    }
}
