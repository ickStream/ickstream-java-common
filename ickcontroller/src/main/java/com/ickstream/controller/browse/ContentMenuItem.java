/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.common.data.StreamingReference;

import java.util.List;

public class ContentMenuItem extends AbstractMenuItem {
    private ContentItem contentItem;

    public ContentMenuItem(ServiceController service, String contextId, ContentItem contentItem) {
        this(service, contextId, contentItem, null);
    }

    public ContentMenuItem(ServiceController service, String contextId, ContentItem contentItem, MenuItem parent) {
        super(service, contextId, parent);
        this.contentItem = contentItem;
    }

    public ContentItem getContentItem() {
        return contentItem;
    }

    @Override
    public String getId() {
        return contentItem.getId();
    }

    @Override
    public String getImage() {
        return contentItem.getImage();
    }

    @Override
    public String getText() {
        return contentItem.getText();
    }

    public List<StreamingReference> getStreamingRefs() {
        return contentItem.getStreamingRefs();
    }

    @Override
    public List<String> getPreferredChildItems() {
        return contentItem.getPreferredChildItems();
    }

    public JsonNode getItemAttributes() {
        return contentItem.getItemAttributes();
    }

    @Override
    public String getType() {
        return contentItem.getType();
    }
}
