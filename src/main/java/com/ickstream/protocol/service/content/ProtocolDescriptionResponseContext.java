/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.List;

public class ProtocolDescriptionResponseContext {
    private String contextId;
    private String name;
    private List<RequestDescription> supportedRequests;

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

    public List<RequestDescription> getSupportedRequests() {
        return supportedRequests;
    }

    public void setSupportedRequests(List<RequestDescription> supportedRequests) {
        this.supportedRequests = supportedRequests;
    }
}
