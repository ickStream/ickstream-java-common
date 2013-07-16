/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class JsonHelper {
    private static final ObjectMapper mapper = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
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

    public Object jsonToObject(JsonNode json, Type objectType) {
        if (json != null) {
            try {
                return mapper.readValue(json.traverse(), mapper.getTypeFactory().constructType(objectType));
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

    public <T> T streamToObject(InputStream stream, Class<T> objectClass) {
        try {
            return mapper.treeToValue(mapper.readTree(stream), objectClass);
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
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void objectToStream(OutputStream output, Object object) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(output, object);
    }

    public JsonNode createObject() {
        return mapper.createObjectNode();
    }

    public JsonNode createObject(String attribute, JsonNode value) {
        if (attribute != null) {
            ObjectNode obj = mapper.createObjectNode();
            obj.put(attribute, value);
            return obj;
        } else if (value != null) {
            return value;
        } else {
            return mapper.createObjectNode();
        }
    }
}
