/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

    public static Object getParamFromJson(String text, String param) throws IOException {
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
                    if (param.equals("id")) {
                        return json;
                    } else {
                        return json.toString();
                    }
                }
            }
        }
        return null;
    }
}
