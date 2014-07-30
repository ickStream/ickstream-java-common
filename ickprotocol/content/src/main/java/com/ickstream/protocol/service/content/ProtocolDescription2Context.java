/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.HashMap;
import java.util.Map;

public class ProtocolDescription2Context {
    private String contextId;
    private String name;
    private Map<String, Map<String, RequestDescription2>> supportedRequests = new HashMap<String, Map<String, RequestDescription2>>();

    public ProtocolDescription2Context() {
    }

    public ProtocolDescription2Context(String contextId, String name) {
        this.contextId = contextId;
        this.name = name;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Map<String, RequestDescription2>> getSupportedRequests() {
        return supportedRequests;
    }

    public void setSupportedRequests(Map<String, Map<String, RequestDescription2>> supportedRequests) {
        this.supportedRequests = supportedRequests;
    }
}
