/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcError {
    public final static int UNAUTHORIZED = -32000;
    public final static int SERVICE_ERROR = -32001;
    public final static int INVALID_JSON = -32700;
    public final static int INVALID_REQUEST = -32600;
    public final static int METHOD_NOT_FOUND = -32601;
    public final static int INVALID_PARAMS = -32602;

    Class<? extends Throwable> exception();

    int code();

    String message();

    String data() default "";
}
