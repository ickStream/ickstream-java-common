/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Transfer object that represents a JSON-RPC request or notification
 */
public class JsonRpcRequest {
    private String jsonrpc = VERSION_2_0;
    private String id;
    private String method;
    private JsonNode params;

    public static String VERSION_2_0 = "2.0";

    /**
     * Creates a new empty instance
     */
    public JsonRpcRequest() {
    }


    /**
     * Creates a new instance using the specified JSON-RPC version and identity
     *
     * @param jsonrpc The JSON-RPC version to use
     * @param id      The request identity for this request
     */
    public JsonRpcRequest(String jsonrpc, String id) {
        this.jsonrpc = jsonrpc;
        this.id = id;
    }

    /**
     * Get the JSON-RPC version of this request
     *
     * @return The JSON-RPC version of this request
     */
    public String getJsonrpc() {
        return jsonrpc;
    }

    /**
     * Set the JSON-RPC version of this request
     *
     * @param jsonrpc The JSON-RPC version of this request
     */
    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    /**
     * Get the JSON-RPC request identity for this request
     *
     * @return The JSON-RPC request identity for this request
     */
    public String getId() {
        return id;
    }

    /**
     * Set the JSON-RPC request identity for this request
     *
     * @param id The JSON-RPC request identity for this request
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the method of the JSON-RPC request or notification
     *
     * @return The method of the JSON-RPC request or notification
     */
    public String getMethod() {
        return method;
    }

    /**
     * Set the method of the JSON-RPC request or notification
     *
     * @param method The method of the JSON-RPC request or notification
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Get the parameters of this JSON-RPC request or notification
     *
     * @return The parameters of the JSON-RPC request or notification
     */
    public JsonNode getParams() {
        return params;
    }

    /**
     * Set the parameters of this JSON-RPC request or notification
     *
     * @param params The parameters of the JSON-RPC request or notification
     */
    public void setParams(JsonNode params) {
        this.params = params;
    }
}
