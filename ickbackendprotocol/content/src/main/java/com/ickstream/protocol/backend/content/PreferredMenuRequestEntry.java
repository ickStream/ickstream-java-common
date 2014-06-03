/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import java.util.List;

public class PreferredMenuRequestEntry extends AbstractPreferredMenuItemEntry {
    private String request;

    public PreferredMenuRequestEntry(String request) {
        this.request = request;
    }

    public PreferredMenuRequestEntry(String request, List<PreferredMenuItemEntry> childItems) {
        super(childItems);
        this.request = request;
    }

    public PreferredMenuRequestEntry(String request, PreferredMenuRequestEntry childRequest) {
        super(childRequest);
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
