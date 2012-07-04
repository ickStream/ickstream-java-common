/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.scrobble;

import com.ickstream.protocol.cloud.content.ContentItem;

public class PlayedItem {
    private Long occurrenceTimestamp;
    private Double playedPercentage;
    private ContentItem track;

    public PlayedItem() {
    }

    public PlayedItem(Long occurrenceTimestamp, Double playedPercentage, ContentItem track) {
        this.occurrenceTimestamp = occurrenceTimestamp;
        this.playedPercentage = playedPercentage;
        this.track = track;
    }

    public Long getOccurrenceTimestamp() {
        return occurrenceTimestamp;
    }

    public void setOccurrenceTimestamp(Long occurrenceTimestamp) {
        this.occurrenceTimestamp = occurrenceTimestamp;
    }

    public Double getPlayedPercentage() {
        return playedPercentage;
    }

    public void setPlayedPercentage(Double playedPercentage) {
        this.playedPercentage = playedPercentage;
    }

    public ContentItem getTrack() {
        return track;
    }

    public void setTrack(ContentItem track) {
        this.track = track;
    }
}
