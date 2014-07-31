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

/**
 * An exception representing a JSON-RPC error
 */
public class JsonRpcException extends Exception {
    private int code;
    private String data;

    /**
     * Create a new empty instance
     */
    public JsonRpcException() {
    }

    /**
     * Create a new instance
     *
     * @param code    The error code, typically one of the codes specified in {@link JsonRpcError}
     * @param message The error message describing the error
     * @param data    Additional data related to the error message
     */
    public JsonRpcException(int code, String message, String data) {
        super(code + ": " + message);
        this.code = code;
        this.data = data;
    }

    /**
     * Get the JSON-RPC error code, this ist typically one of the codes specified in {@link JsonRpcError}
     *
     * @return The JSON-RPC error code
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the additional JSON-RPC error data provided with the error
     *
     * @return The additional data provided with the error
     */
    public String getData() {
        return data;
    }
}
