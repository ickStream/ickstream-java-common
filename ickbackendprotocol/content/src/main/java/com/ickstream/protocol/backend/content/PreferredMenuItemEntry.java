/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import com.ickstream.protocol.service.ImageReference;

import java.util.List;

public class PreferredMenuItemEntry extends AbstractPreferredMenuItemEntry {
    private String contextId;
    private PreferredMenuItemType type;
    private String textValue;
    private String textKey;
    private String resourceBundle;
    private String menuType;
    private List<ImageReference> images;

    public static PreferredMenuItemEntry createLocalized(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, null, null, childItems);
    }

    public static PreferredMenuItemEntry createLocalized(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, null, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, null, images, childItems);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, null, images, childRequest);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(String contextId, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, null, images, childItems);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(String contextId, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, null, images, childRequest);
    }

    public static PreferredMenuItemEntry create(String contextId, PreferredMenuItemType type, String textValue, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, null, null, childItems);
    }

    public static PreferredMenuItemEntry create(String contextId, PreferredMenuItemType type, String textValue, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, null, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalized(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, null, null, childItems);
    }

    public static PreferredMenuItemEntry createLocalized(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, null, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, null, images, childItems);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, null, images, childRequest);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(Context context, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, null, images, childItems);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(Context context, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, null, images, childRequest);
    }

    public static PreferredMenuItemEntry create(Context context, PreferredMenuItemType type, String textValue, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, null, null, childItems);
    }

    public static PreferredMenuItemEntry create(Context context, PreferredMenuItemType type, String textValue, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, null, null, childRequest);
    }

    private PreferredMenuItemEntry(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String textValue, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        super(childRequest);
        this.contextId = contextId;
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.textValue = textValue;
        this.menuType = menuType;
        this.images = images;
    }

    private PreferredMenuItemEntry(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String textValue, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        super(childItems);
        this.contextId = contextId;
        this.type = type;
        this.resourceBundle = resourceBundle;
        this.textKey = textKey;
        this.textValue = textValue;
        this.menuType = menuType;
        this.images = images;
    }

    public String getContextId() {
        return contextId;
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

    public String getMenuType() {
        return menuType;
    }
}
