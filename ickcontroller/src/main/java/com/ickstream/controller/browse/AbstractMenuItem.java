/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;

public abstract class AbstractMenuItem implements MenuItem {
    private ServiceController serviceController;
    private String contextId;
    private MenuItem parent;

    public AbstractMenuItem(ServiceController serviceController, String contextId) {
        this.serviceController = serviceController;
        this.contextId = contextId;
    }

    public AbstractMenuItem(ServiceController serviceController, String contextId, MenuItem parent) {
        this.serviceController = serviceController;
        this.contextId = contextId;
        this.parent = parent;
    }

    public ServiceController getServiceController() {
        return serviceController;
    }

    public String getContextId() {
        return contextId;
    }

    public MenuItem getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return getText();
    }
}
