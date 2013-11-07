/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.node.IntNode;

/**
 * A JSON-RPC identity provider which generates unique identities within this identity provider instance
 * Please note that since JSON-RPC identities needs to be unique for the same communication channel, it's often easier
 * to use {@link GlobalIdProvider} than {@link InstanceIdProvider}.
 */
public class InstanceIdProvider implements IdProvider {
    private int id = 0;
    private final Object syncObject = new Object();

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
