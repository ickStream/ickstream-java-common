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
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * Transfer object that represents a JSON-RPC request or notification
 */
public class JsonRpcRequest {
    private String jsonrpc = VERSION_2_0;
    private ValueNode id;
    private String method;
    private JsonNode params;

    public static String VERSION_2_0 = "2.0";

    /**
     * Creates a new empty instance
     */
    public JsonRpcRequest() {
    }


    /**
     * Creates a new instance using the specified JSON-RPC version
     *
     * @param jsonrpc The JSON-RPC version to use
     */
    public JsonRpcRequest(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    /**
     * Creates a new instance using the specified JSON-RPC version and identity
     *
     * @param jsonrpc The JSON-RPC version to use
     * @param id      The request identity for this request
     */
    public JsonRpcRequest(String jsonrpc, String id) {
        this.jsonrpc = jsonrpc;
        this.id = new TextNode(id);
    }

    /**
     * Creates a new instance using the specified JSON-RPC version and identity
     *
     * @param jsonrpc The JSON-RPC version to use
     * @param id      The request identity for this request
     */
    public JsonRpcRequest(String jsonrpc, int id) {
        this.jsonrpc = jsonrpc;
        this.id = new IntNode(id);
    }

    /**
     * Creates a new instance using the specified JSON-RPC version and identity
     *
     * @param jsonrpc The JSON-RPC version to use
     * @param id      The request identity for this request
     */
    public JsonRpcRequest(String jsonrpc, ValueNode id) {
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
    public ValueNode getId() {
        return id;
    }

    /**
     * Set the JSON-RPC request identity for this request
     *
     * @param id The JSON-RPC request identity for this request
     */
    public void setId(ValueNode id) {
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
