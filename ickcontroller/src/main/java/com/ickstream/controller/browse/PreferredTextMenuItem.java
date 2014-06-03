/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.service.content.AbstractPreferredMenu;

import java.util.List;

public class PreferredTextMenuItem extends AbstractMenuItem implements AbstractPreferredMenuItem {
    public final static String TYPE = "text";
    private AbstractPreferredMenu preferredMenuItem;
    private String text;
    private String id;

    public PreferredTextMenuItem(AbstractPreferredMenu preferredMenuItem, ServiceController serviceController, String text) {
        super(serviceController, null);
        this.preferredMenuItem = preferredMenuItem;
        this.text = text;
        this.id = text;
    }

    public PreferredTextMenuItem(AbstractPreferredMenu preferredMenuItem, ServiceController serviceController, String text, MenuItem parent) {
        super(serviceController, null, parent);
        this.preferredMenuItem = preferredMenuItem;
        this.text = text;
        if (parent != null) {
            this.id = parent.getId() + "." + text;
        } else {
            this.id = text;
        }
    }

    @Override
    public List<String> getPreferredChildItems() {
        return null;
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
        return null;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public AbstractPreferredMenu getPreferredMenuItem() {
        return preferredMenuItem;
    }
}
