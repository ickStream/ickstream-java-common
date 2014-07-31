/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.content;

public enum Context {
    MY_MUSIC("myMusic"),
    ALL_MUSIC("allMusic"),
    HOT_MUSIC("hotMusic"),
    FRIENDS_MUSIC("friendsMusic"),
    ALL_RADIO("allRadio");

    private String name;

    private Context(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
