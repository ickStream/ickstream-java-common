/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;
import com.ickstream.protocol.common.data.ContentItem;
import com.ickstream.protocol.service.content.AbstractPreferredMenu;

public class PreferredContentMenuItem extends ContentMenuItem implements AbstractPreferredMenuItem {
    private AbstractPreferredMenu preferredMenuItem;

    public PreferredContentMenuItem(AbstractPreferredMenu preferredMenuItem, ServiceController service, String contextId, ContentItem contentItem) {
        super(service, contextId, contentItem);
        this.preferredMenuItem = preferredMenuItem;
    }

    public PreferredContentMenuItem(AbstractPreferredMenu preferredMenuItem, ServiceController service, String contextId, ContentItem contentItem, MenuItem parent) {
        super(service, contextId, contentItem, parent);
        this.preferredMenuItem = preferredMenuItem;
    }

    public AbstractPreferredMenu getPreferredMenuItem() {
        return preferredMenuItem;
    }
}
