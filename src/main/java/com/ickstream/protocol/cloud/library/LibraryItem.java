/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.library;

import com.ickstream.protocol.cloud.content.ContentItem;

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
