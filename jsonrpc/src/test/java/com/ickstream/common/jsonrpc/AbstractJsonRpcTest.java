/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public abstract class AbstractJsonRpcTest {
    public static String createJsonRequest(String id, String method, String params) {
        return createJsonRequest(JsonRpcRequest.VERSION_2_0, id, method, params);
    }

    public static String createJsonRequest(String version, String id, String method, String params) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (version != null) {
            sb.append("\"jsonrpc\":\"" + version + "\",");
        }
        if (id != null) {
            sb.append("\"id\":\"").append(id).append("\"");
        } else {
            sb.append("\"id\":").append(1);
        }
        if (method != null) {
            sb.append(",").append("\"method\":\"").append(method).append("\"");
        }
        if (params != null) {
            sb.append(",");
            sb.append("\"params\":").append(params).append("");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String createJsonNotification(String method, String params) {
        return createJsonNotification(JsonRpcRequest.VERSION_2_0, method, params);
    }

    public static String createJsonNotification(String version, String method, String params) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (version != null) {
            sb.append("\"jsonrpc\":\"" + version + "\"");
        }
        if (method != null) {
            sb.append(",").append("\"method\":\"").append(method).append("\"");
        }
        if (params != null) {
            sb.append(",");
            sb.append("\"params\":").append(params).append("");
        }
        sb.append("}");
        return sb.toString();
    }

    public static String getParamFromJson(String text, String param) throws IOException {
        if (text != null) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(text);
            String[] params = param.split("\\.");
            for (int i = 0; i < params.length; i++) {
                if (json.isArray()) {
                    json = json.get(new Integer(params[i]));
                } else if (json.has(params[i])) {
                    json = json.get(params[i]);
                } else {
                    return null;
                }
            }
            if (json != null) {
                if (json.isTextual()) {
                    return json.asText();
                } else {
                    return json.toString();
                }
            }
        }
        return null;
    }
}
