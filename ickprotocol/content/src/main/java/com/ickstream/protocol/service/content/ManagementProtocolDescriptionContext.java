/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.ArrayList;
import java.util.List;

public class ManagementProtocolDescriptionContext {
    private String contextId;
    private List<String> supportedTypes = new ArrayList<String>();

    public ManagementProtocolDescriptionContext() {
    }

    public ManagementProtocolDescriptionContext(String contextId) {
        this.contextId = contextId;
    }

    public String getContextId() {
        return contextId;
    }

    public void setContextId(String contextId) {
        this.contextId = contextId;
    }

    public List<String> getSupportedTypes() {
        return supportedTypes;
    }

    public void setSupportedTypes(List<String> supportedTypes) {
        this.supportedTypes = supportedTypes;
    }
}
