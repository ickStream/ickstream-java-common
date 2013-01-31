/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

public class JsonHelper {
    private ObjectMapper mapper;

    public JsonHelper() {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public <T> T jsonToObject(JsonNode json, Class<T> objectClass) {
        if (json != null) {
            try {
                return mapper.treeToValue(json, objectClass);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public <T> T stringToObject(String text, Class<T> objectClass) {
        try {
            return mapper.treeToValue(mapper.readTree(text), objectClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public JsonNode objectToJson(Object object) {
        return mapper.valueToTree(object);
    }

    public String objectToString(Object object) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectToJson(object));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
