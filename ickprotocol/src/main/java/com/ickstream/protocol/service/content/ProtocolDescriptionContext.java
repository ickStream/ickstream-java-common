/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.protocol.service.ImageReference;

import java.util.ArrayList;
import java.util.List;

public class ProtocolDescriptionContext {
    private String contextId;
    private String name;
    private List<ImageReference> images = new ArrayList<ImageReference>();
    private List<RequestDescription> supportedRequests = new ArrayList<RequestDescription>();

    public ProtocolDescriptionContext() {
    }

    public ProtocolDescriptionContext(String contextId, String name) {
        this.contextId = contextId;
        this.name = name;
    }

    public ProtocolDescriptionContext(String contextId, String name, List<ImageReference> images) {
        this.contextId = contextId;
        this.name = name;
        this.images = images;
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

    public List<RequestDescription> getSupportedRequests() {
        return supportedRequests;
    }

    public void setSupportedRequests(List<RequestDescription> supportedRequests) {
        this.supportedRequests = supportedRequests;
    }

    public List<ImageReference> getImages() {
        return images;
    }

    public void setImages(List<ImageReference> images) {
        this.images = images;
    }
}
