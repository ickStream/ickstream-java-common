/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.List;

public abstract class AbstractPreferredMenu {
    private List<PreferredMenuItem> childItems;
    private PreferredMenuRequest childRequest;

    protected AbstractPreferredMenu() {
    }

    protected AbstractPreferredMenu(List<PreferredMenuItem> childItems) {
        this.childItems = childItems;
    }

    protected AbstractPreferredMenu(PreferredMenuRequest childRequest) {
        this.childRequest = childRequest;
    }

    public List<PreferredMenuItem> getChildItems() {
        return childItems;
    }

    public void setChildItems(List<PreferredMenuItem> childItems) {
        this.childItems = childItems;
    }

    public PreferredMenuRequest getChildRequest() {
        return childRequest;
    }

    public void setChildRequest(PreferredMenuRequest childRequest) {
        this.childRequest = childRequest;
    }
}
