/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import com.ickstream.protocol.service.ImageReference;

import java.util.List;

public class PreferredMenuItemEntry extends AbstractPreferredMenuItemEntry {
    private PreferredMenuItemType type;
    private String textValue;
    private String textKey;
    private String resourceBundle;
    private String imageType;
    private List<ImageReference> images;

    public static PreferredMenuItemEntry createLocalized(PreferredMenuItemType type, String resourceBundle, String textKey, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(type, resourceBundle, textKey, null, null, null, childItems);
    }

    public static PreferredMenuItemEntry createLocalized(PreferredMenuItemType type, String resourceBundle, String textKey, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(type, resourceBundle, textKey, null, null, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(PreferredMenuItemType type, String resourceBundle, String textKey, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(type, resourceBundle, textKey, null, null, images, childItems);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(PreferredMenuItemType type, String resourceBundle, String textKey, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(type, resourceBundle, textKey, null, null, images, childRequest);
    }

    public static PreferredMenuItemEntry createLocalizedWithImageType(PreferredMenuItemType type, String resourceBundle, String textKey, String imageType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(type, resourceBundle, textKey, null, imageType, null, childItems);
    }

    public static PreferredMenuItemEntry createLocalizedWithImageType(PreferredMenuItemType type, String resourceBundle, String textKey, String imageType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(type, resourceBundle, textKey, null, imageType, null, childRequest);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(PreferredMenuItemType type, String textValue, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(type, null, null, textValue, null, images, childItems);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(PreferredMenuItemType type, String textValue, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(type, null, null, textValue, null, images, childRequest);
    }

    public static PreferredMenuItemEntry createWithImageType(PreferredMenuItemType type, String textValue, String imageType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(type, null, null, textValue, imageType, null, childItems);
    }

    public static PreferredMenuItemEntry createWithImageType(PreferredMenuItemType type, String textValue, String imageType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(type, null, null, textValue, imageType, null, childRequest);
    }

    public static PreferredMenuItemEntry create(PreferredMenuItemType type, String textValue, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(type, null, null, textValue, null, null, childItems);
    }

    public static PreferredMenuItemEntry create(PreferredMenuItemType type, String textValue, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(type, null, null, textValue, null, null, childRequest);
    }

    private PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, String textValue, String imageType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        super(childRequest);
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.textValue = textValue;
        this.imageType = imageType;
        this.images = images;
    }

    private PreferredMenuItemEntry(PreferredMenuItemType type, String resourceBundle, String textKey, String textValue, String imageType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        super(childItems);
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.textValue = textValue;
        this.imageType = imageType;
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

    public String getTextValue() {
        return textValue;
    }

    public List<ImageReference> getImages() {
        return images;
    }

    public String getImageType() {
        return imageType;
    }
}
