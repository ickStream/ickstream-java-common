/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller;

public interface ObjectChangeListener<T> {
    void onRemoved(T object);

    void onAdded(T object);
}
