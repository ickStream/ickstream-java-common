/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Message logger implementation that logs to standard output
 */
public class ConsoleMessageLogger implements MessageLogger {
    private JsonHelper jsonHelper = new JsonHelper();

    /**
     * Logs an outgoing message to the standard output
     *
     * @param destination The destination the message is sent to
     * @param message     The message sent
     */
    @Override
    public void onOutgoingMessage(String destination, String message) {
        System.out.println("Outgoing (" + destination + "): \n" + message + "\n\n");
    }

    /**
     * Logs an incoming message to the standard output
     *
     * @param source  The source which the message came from
     * @param message The message received
     */
    @Override
    public void onIncomingMessage(String source, String message) {
        JsonNode jsonNode = jsonHelper.stringToObject(message, JsonNode.class);
        if (jsonNode == null) {
            System.out.println("Incoming (" + source + "): \n" + message + "\n\n");
        } else {
            System.out.println("Incoming (" + source + "): \n" + jsonHelper.objectToString(jsonNode) + "\n\n");
        }
    }
}
