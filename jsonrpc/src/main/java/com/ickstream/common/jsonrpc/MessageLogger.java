/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

/**
 * Represents a message logger implementation that can log incoming and outgoing messages
 */
public interface MessageLogger {
    /**
     * Log an outgoing message
     *
     * @param destination The destination the message is sent to
     * @param message     The message sent
     */
    void onOutgoingMessage(String destination, String message);

    /**
     * Log an incoming message
     *
     * @param source  The source which the message came from
     * @param message The message received
     */
    void onIncomingMessage(String source, String message);
}
