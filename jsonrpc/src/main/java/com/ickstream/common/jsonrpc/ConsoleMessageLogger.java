/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;

public class ConsoleMessageLogger implements MessageLogger {
    private JsonHelper jsonHelper = new JsonHelper();

    @Override
    public void onOutgoingMessage(String destination, String message) {
        System.out.println("Outgoing (" + destination + "): \n" + message + "\n\n");
    }

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
