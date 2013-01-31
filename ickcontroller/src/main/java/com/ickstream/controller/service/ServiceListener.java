/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

public interface ServiceListener {
    void onServiceAdded(ServiceController serviceController);

    void onServiceRemoved(ServiceController serviceController);
}
