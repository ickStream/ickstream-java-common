/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

public enum PreferredMenuItemType {
    SEARCH("search"),
    BROWSE("browse");

    private String name;

    private PreferredMenuItemType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
