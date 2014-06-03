/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

public interface BrowseMenu {
    public interface ResponseListener<T> {
        void onResponse(T contentResponse);
    }

    void findChilds(String language, MenuItem contentItem, ResponseListener<BrowseResponse> listener);
}
