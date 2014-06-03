/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPreferredMenuItemEntry {
    private List<PreferredMenuItemEntry> childItems = new ArrayList<PreferredMenuItemEntry>();
    private PreferredMenuRequestEntry childRequest;

    protected AbstractPreferredMenuItemEntry() {
    }

    protected AbstractPreferredMenuItemEntry(List<PreferredMenuItemEntry> childItems) {
        this.childItems = childItems;
    }

    protected AbstractPreferredMenuItemEntry(PreferredMenuRequestEntry childRequest) {
        this.childRequest = childRequest;
    }

    public List<PreferredMenuItemEntry> getChildItems() {
        return childItems;
    }

    public PreferredMenuRequestEntry getChildRequest() {
        return childRequest;
    }
}