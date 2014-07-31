/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
