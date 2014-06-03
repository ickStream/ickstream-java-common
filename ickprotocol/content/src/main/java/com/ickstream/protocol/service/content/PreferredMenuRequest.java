/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.List;

public class PreferredMenuRequest extends AbstractPreferredMenu {
    private String request;

    public PreferredMenuRequest() {
    }

    public PreferredMenuRequest(String request) {
        this.request = request;
    }

    public PreferredMenuRequest(String request, List<PreferredMenuItem> childItems) {
        super(childItems);
        this.request = request;
    }

    public PreferredMenuRequest(String request, PreferredMenuRequest childRequest) {
        super(childRequest);
        this.request = request;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
