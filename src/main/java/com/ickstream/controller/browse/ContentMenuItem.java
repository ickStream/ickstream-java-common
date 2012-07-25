/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.common.data.StreamingReference;
import org.codehaus.jackson.JsonNode;

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

    public String getId() {
        return contentItem.getId();
    }

    public String getImage() {
        return contentItem.getImage();
    }

    public String getText() {
        return contentItem.getText();
    }

    public List<StreamingReference> getStreamingRefs() {
        return contentItem.getStreamingRefs();
    }

    public JsonNode getItemAttributes() {
        return contentItem.getItemAttributes();
    }

    public String getType() {
        return contentItem.getType();
    }
}
