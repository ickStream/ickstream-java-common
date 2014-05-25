/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

public interface BrowseMenu {
    public interface ResponseListener<T> {
        void onResponse(T contentResponse);
    }

    void findContexts(String language, ResponseListener<BrowseResponse> listener);

    void findItemsInContext(String contextId, String language, MenuItem contentItem, ResponseListener<BrowseResponse> listener);

    void findItemsInContextByType(String contextId, String language, String type, MenuItem contentItem, ResponseListener<BrowseResponse> listener);
}
