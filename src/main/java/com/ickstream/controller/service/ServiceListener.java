/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.service;

public interface ServiceListener {
    void onServiceAdded(ServiceController serviceController);

    void onServiceRemoved(ServiceController serviceController);
}
