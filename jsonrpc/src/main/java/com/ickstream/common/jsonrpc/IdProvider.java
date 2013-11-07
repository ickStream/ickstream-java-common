/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * Identity provider that generates identities to be used in JSON-RPC requests
 */
public interface IdProvider {
    /**
     * Get a new identity to use in a JSON-RPC request
     *
     * @return The identity to use, {@link Object#toString()} will be called on this object to produce a String identity
     */
    ValueNode getNextId();
}
