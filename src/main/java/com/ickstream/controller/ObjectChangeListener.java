/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller;

public interface ObjectChangeListener<T> {
    void onRemoved(T object);

    void onAdded(T object);
}
