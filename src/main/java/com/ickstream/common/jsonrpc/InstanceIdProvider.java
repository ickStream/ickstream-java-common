/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

public class InstanceIdProvider implements IdProvider {
    private int id = 0;
    private final Object syncObject = new Object();

    @Override
    public Object getNextId() {
        synchronized (syncObject) {
            return ++id;
        }
    }
}
