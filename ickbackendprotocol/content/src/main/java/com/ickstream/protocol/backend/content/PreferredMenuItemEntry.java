/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import java.util.List;

public class PreferredMenuItemEntry extends AbstractPreferredMenuItemEntry {
    private PreferredMenuItemType type;
    private String textKey;
    private String resourceBundle;
    private String image;

    public PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, String image) {
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.image = image;
    }

    public PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, String image, List<PreferredMenuItemEntry> childItems) {
        super(childItems);
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.image = image;
    }

    public PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, String image, PreferredMenuRequestEntry childRequest) {
        super(childRequest);
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.image = image;
    }

    public PreferredMenuItemType getType() {
        return type;
    }

    public String getTextKey() {
        return textKey;
    }

    public String getResourceBundle() {
        return resourceBundle;
    }

    public String getImage() {
        return image;
    }

}
