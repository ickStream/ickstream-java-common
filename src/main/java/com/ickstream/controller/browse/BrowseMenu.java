/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.browse;

public interface BrowseMenu {
    public interface ResponseListener<T> {
        void onResponse(T contentResponse);
    }

    void findContexts(ResponseListener<BrowseResponse> listener);

    void findItemsInContext(String contextId, MenuItem contentItem, ResponseListener<BrowseResponse> listener);

    void findItemsInContextByType(String contextId, String type, MenuItem contentItem, ResponseListener<BrowseResponse> listener);
}
