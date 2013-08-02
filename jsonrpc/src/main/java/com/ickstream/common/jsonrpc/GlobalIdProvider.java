/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * A JSON-RPC identity provider which generates unique identities within this JVM instance
 */
public class GlobalIdProvider implements IdProvider {
    private static int id = 0;
    private final static Object syncObject = new Object();

    /**
     * See {@link com.ickstream.common.jsonrpc.IdProvider#getNextId()}
     */
    @Override
    public Integer getNextId() {
        synchronized (syncObject) {
            return ++id;
        }
    }
}
