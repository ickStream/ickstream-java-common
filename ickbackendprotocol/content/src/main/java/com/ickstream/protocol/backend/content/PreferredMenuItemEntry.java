/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import com.ickstream.protocol.service.ImageReference;

import java.util.List;

public class PreferredMenuItemEntry extends AbstractPreferredMenuItemEntry {
    private PreferredMenuItemType type;
    private String textKey;
    private String resourceBundle;
    private List<ImageReference> images;

    public PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, List<ImageReference> images) {
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.images = images;
    }

    public PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        super(childItems);
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.images = images;
    }

    public PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        super(childRequest);
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.images = images;
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

    public List<ImageReference> getImages() {
        return images;
    }

}
