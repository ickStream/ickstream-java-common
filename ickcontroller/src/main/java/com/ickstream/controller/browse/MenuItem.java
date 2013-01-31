/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;

public interface MenuItem {
    ServiceController getServiceController();

    MenuItem getParent();

    String getContextId();

    String getId();

    String getText();

    String getImage();

    String getType();
}
