/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.node.IntNode;

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
    public IntNode getNextId() {
        synchronized (syncObject) {
            return new IntNode(++id);
        }
    }
}
