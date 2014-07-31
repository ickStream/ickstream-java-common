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
import com.fasterxml.jackson.databind.node.ValueNode;

/**
 * Transfer object that represents a JSON-RPC response
 */
public class JsonRpcResponse {
    private String jsonrpc = JsonRpcRequest.VERSION_2_0;
    private ValueNode id;
    private JsonNode result;
    private Error error;

    /**
     * Transfer object that represents a JSON-RPC error
     */
    public static class Error {
        private int code;
        private String message;
        private String data;

        /**
         * Create a new empty instance
         */
        public Error() {
        }

        /**
         * Create a new instance using the specified error code and message
         *
         * @param code    The error code, typically one which is specified by {@link JsonRpcError}
         * @param message The error message that describes the error
         */
        public Error(int code, String message) {
            this.code = code;
            this.message = message;
        }

        /**
         * Create a new instance using the specified error code, message and additinoal data
         *
         * @param code    The error code, typically one which is specified by {@link JsonRpcError}
         * @param message The error message that describes the error
         * @param data    Additional data related to this error
         */
        public Error(int code, String message, String data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        /**
         * Get the error code of this error
         *
         * @return The error code of this error, typically one specified by {@link JsonRpcError}
         */
        public int getCode() {
            return code;
        }

        /**
         * Set the error code of this error
         *
         * @param code The error code of this error, typically one specified by {@link JsonRpcError}
         */
        public void setCode(int code) {
            this.code = code;
        }

        /**
         * Get the error message
         *
         * @return The error message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Set the error message
         *
         * @param message Set the error message
         */
        public void setMessage(String message) {
            this.message = message;
        }

        /**
         * Get the additional data related to this error
         *
         * @return The additional data
         */
        public String getData() {
            return data;
        }

        /**
         * Set the additional data related to this error
         *
         * @param data The additional data
         */
        public void setData(String data) {
            this.data = data;
        }
    }

    /**
     * Creates a new empty instance
     */
    public JsonRpcResponse() {
    }

    /**
     * Creates a new instance using the specified JSON-RPC version and identity
     *
     * @param jsonrpc The JSON-RPC version to use
     * @param id      The request identity for this response
     */
    public JsonRpcResponse(String jsonrpc, ValueNode id) {
        this.jsonrpc = jsonrpc;
        this.id = id;
    }

    /**
     * Get the JSON-RPC version of this response
     *
     * @return The JSON-RPC version of this response
     */
    public String getJsonrpc() {
        return jsonrpc;
    }

    /**
     * Set the JSON-RPC version of this response
     *
     * @param jsonrpc The JSON-RPC version of this response
     */
    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    /**
     * Get the JSON-RPC request identity for this response
     *
     * @return The JSON-RPC request identity for this response
     */
    public ValueNode getId() {
        return id;
    }

    /**
     * Set the JSON-RPC request identity for this response
     *
     * @param id The JSON-RPC request identity for this response
     */
    public void setId(ValueNode id) {
        this.id = id;
    }

    /**
     * Get the JSON-RPC response result
     *
     * @return The JSON-RPC response result
     */
    public JsonNode getResult() {
        return result;
    }

    /**
     * Set the JSON-RPC response result
     *
     * @param result The JSON-RPC response result
     */
    public void setResult(JsonNode result) {
        this.result = result;
    }

    /**
     * Get the JSON-RPC response error information
     *
     * @return The JSON-RPC response error information
     */
    public Error getError() {
        return error;
    }

    /**
     * Set the JSON-RPC response error information
     *
     * @param error The JSON-RPC response error information
     */
    public void setError(Error error) {
        this.error = error;
    }
}
