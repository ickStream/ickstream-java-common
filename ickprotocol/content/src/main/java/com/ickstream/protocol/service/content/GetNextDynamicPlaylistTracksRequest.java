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
