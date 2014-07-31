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
