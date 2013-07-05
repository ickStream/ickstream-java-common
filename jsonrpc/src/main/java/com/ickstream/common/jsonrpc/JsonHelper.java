/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonHelper {
    private static ObjectMapper mapper;

    {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public JsonHelper() {
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
