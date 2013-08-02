/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that is used to specify which error code a specific exception should result in.
 * This annotation is used by {@link StreamJsonRpcService} or one of its implementations to create the appropriate
 * JSON-RPC response message after an exception has been thrown
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcError {
    /**
     * The caller was not authorized to make the specified JSON-RPC request
     */
    public final static int UNAUTHORIZED = -32000;
    /**
     * An internal error has occurred inside the service implementing the processing of the JSON-RPC request
     */
    public final static int SERVICE_ERROR = -32001;
    /**
     * The request contained invalid JSON data
     */
    public final static int INVALID_JSON = -32700;
    /**
     * The request was not valid JSON-RPC request
     */
    public final static int INVALID_REQUEST = -32600;
    /**
     * The request method does not exist
     */
    public final static int METHOD_NOT_FOUND = -32601;
    /**
     * The specified list of parameters to the request is invalid
     */
    public final static int INVALID_PARAMS = -32602;

    /**
     * @return The exception thrown
     */
    Class<? extends Throwable> exception();

    /**
     * The error code to use in JSON-RPC response message
     */
    int code();

    /**
     * The error description to use in JSON-RPC response message
     */
    String message();

    /**
     * The additional data about the error to use in JSON-RPC response message
     */
    String data() default "";
}
