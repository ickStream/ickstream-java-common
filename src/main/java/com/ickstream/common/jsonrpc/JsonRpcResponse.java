/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import org.codehaus.jackson.JsonNode;

public class JsonRpcResponse {
    private String jsonrpc = JsonRpcRequest.VERSION_2_0;
    private String id;
    private JsonNode result;
    private Error error;

    public static class Error {
        private int code;
        private String message;
        private String data;

        public Error() {
        }

        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public Error(int code, String message, String data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public JsonRpcResponse() {
    }

    public JsonRpcResponse(String jsonrpc, String id) {
        this.jsonrpc = jsonrpc;
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JsonNode getResult() {
        return result;
    }

    public void setResult(JsonNode result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
