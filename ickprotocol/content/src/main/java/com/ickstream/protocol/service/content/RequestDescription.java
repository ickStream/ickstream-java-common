/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import java.util.ArrayList;
import java.util.List;

public class RequestDescription {
    private String type;
    private List<List<String>> parameters = new ArrayList<List<String>>();

    public RequestDescription() {
    }

    public RequestDescription(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<List<String>> getParameters() {
        return parameters;
    }

    public void setParameters(List<List<String>> parameters) {
        this.parameters = parameters;
    }
}
