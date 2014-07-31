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

package com.ickstream.protocol.service.library;

import com.ickstream.protocol.common.data.ContentItem;

import java.util.List;

public class LibraryItem {
    private Long lastUpdated;
    private Boolean loved;
    private Boolean banned;
    private Integer rating;
    private List<String> tags;
    private List<String> tagsToAdd;
    private List<String> tagsToRemove;
    private ContentItem track;

    public LibraryItem() {
    }

    public LibraryItem(ContentItem track) {
        this.track = track;
    }

    public LibraryItem(ContentItem track, Long lastUpdated) {
        this.track = track;
        this.lastUpdated = lastUpdated;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getLoved() {
        return loved;
    }

    public void setLoved(Boolean loved) {
        this.loved = loved;
    }

    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTagsToAdd() {
        return tagsToAdd;
    }

    public void setTagsToAdd(List<String> tagsToAdd) {
        this.tagsToAdd = tagsToAdd;
    }

    public List<String> getTagsToRemove() {
        return tagsToRemove;
    }

    public void setTagsToRemove(List<String> tagsToRemove) {
        this.tagsToRemove = tagsToRemove;
    }

    public ContentItem getTrack() {
        return track;
    }

    public void setTrack(ContentItem track) {
        this.track = track;
    }
}
