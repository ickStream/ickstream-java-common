/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * A message sender which is able to send a JSON-RPC message using an appropriate communication protocol.
 * The actual communication protocol is decided on the class implementing this interface.
 */
public interface MessageSender {
    /**
     * Send a message
     *
     * @param message The message to send
     */
    void sendMessage(String message);
}
