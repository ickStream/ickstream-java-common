/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, menuType, null, childItems);
    }

    public static PreferredMenuItemEntry createLocalized(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, menuType, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, menuType, images, childItems);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(String contextId, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, resourceBundle, textKey, null, menuType, images, childRequest);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(String contextId, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, menuType, images, childItems);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(String contextId, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, menuType, images, childRequest);
    }

    public static PreferredMenuItemEntry create(String contextId, PreferredMenuItemType type, String textValue, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, menuType, null, childItems);
    }

    public static PreferredMenuItemEntry create(String contextId, PreferredMenuItemType type, String textValue, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(contextId, type, null, null, textValue, menuType, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalized(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, menuType, null, childItems);
    }

    public static PreferredMenuItemEntry createLocalized(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, menuType, null, childRequest);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, menuType, images, childItems);
    }

    public static PreferredMenuItemEntry createLocalizedWithExplicitImage(Context context, PreferredMenuItemType type, String resourceBundle, String textKey, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, resourceBundle, textKey, null, menuType, images, childRequest);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(Context context, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, menuType, images, childItems);
    }

    public static PreferredMenuItemEntry createWithExplicitImage(Context context, PreferredMenuItemType type, String textValue, String menuType, List<ImageReference> images, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, menuType, images, childRequest);
    }

    public static PreferredMenuItemEntry create(Context context, PreferredMenuItemType type, String textValue, String menuType, List<PreferredMenuItemEntry> childItems) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, menuType, null, childItems);
    }

    public static PreferredMenuItemEntry create(Context context, PreferredMenuItemType type, String textValue, String menuType, PreferredMenuRequestEntry childRequest) {
        return new PreferredMenuItemEntry(context.toString(), type, null, null, textValue, menuType, null, childRequest);
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
