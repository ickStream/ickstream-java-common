/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

public class DefaultIdProvider implements IdProvider {
    private static int id = 0;
    private final static Object syncObject = new Object();

    @Override
    public Integer getNextId() {
        synchronized (syncObject) {
            return ++id;
        }
    }
}
