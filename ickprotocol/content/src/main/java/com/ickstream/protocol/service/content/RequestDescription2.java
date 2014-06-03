/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */
package com.ickstream.protocol.service.content;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class RequestDescription2 {
    private List<String> parameters = new ArrayList<String>();
    private JsonNode values;

    public RequestDescription2() {
    }

    public RequestDescription2(List<String> parameters) {
        this.parameters = parameters;
    }

    public RequestDescription2(List<String> parameters, JsonNode values) {
        this.parameters = parameters;
        this.values = values;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public JsonNode getValues() {
        return values;
    }

    public void setValues(JsonNode values) {
        this.values = values;
    }
}
