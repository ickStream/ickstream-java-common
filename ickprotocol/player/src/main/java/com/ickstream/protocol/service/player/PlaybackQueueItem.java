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

package com.ickstream.protocol.service.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.protocol.common.data.StreamingReference;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.List;

public class PlaybackQueueItem {
    private String id;
    private String text;
    private String type;
    private String image;
    private List<StreamingReference> streamingRefs;
    private JsonNode itemAttributes;

    public PlaybackQueueItem() {
    }

    public PlaybackQueueItem(String id, String text, String type, String image) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.image = image;
    }

    public PlaybackQueueItem(String id, String text, String type, String image, List<StreamingReference> streamingRefs) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.image = image;
        this.streamingRefs = streamingRefs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<StreamingReference> getStreamingRefs() {
        return streamingRefs;
    }

    public void setStreamingRefs(List<StreamingReference> streamingRefs) {
        this.streamingRefs = streamingRefs;
    }

    public JsonNode getItemAttributes() {
        return itemAttributes;
    }

    public void setItemAttributes(JsonNode itemAttributes) {
        this.itemAttributes = itemAttributes;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlaybackQueueItem)) {
            return false;
        }
        return new EqualsBuilder().append(id, ((PlaybackQueueItem) o).getId()).isEquals();
    }
}
