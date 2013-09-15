/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.common.data.ContentItem;

import java.util.List;

public class GetNextDynamicPlaylistTracksRequest {
    private Integer count;
    private List<ContentItem> previousItems;
    private SelectionParameters selectionParameters;

    public GetNextDynamicPlaylistTracksRequest() {
    }

    public GetNextDynamicPlaylistTracksRequest(Integer count, SelectionParameters selectionParameters) {
        this.count = count;
        this.selectionParameters = selectionParameters;
    }

    public GetNextDynamicPlaylistTracksRequest(Integer count, SelectionParameters selectionParameters, List<ContentItem> previousItems) {
        this.count = count;
        this.previousItems = previousItems;
        this.selectionParameters = selectionParameters;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ContentItem> getPreviousItems() {
        return previousItems;
    }

    public void setPreviousItems(List<ContentItem> previousItems) {
        this.previousItems = previousItems;
    }

    public SelectionParameters getSelectionParameters() {
        return selectionParameters;
    }

    public void setSelectionParameters(SelectionParameters selectionParameters) {
        this.selectionParameters = selectionParameters;
    }
}
