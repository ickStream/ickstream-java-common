/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation representing the name of the attribute within the JSON-RPC response result where the response value
 * can be found. This is used by {@link StreamJsonRpcService} or one of its implementations to create the
 * JSON-RPC response structure. This annotation is optional and typically only have to be specified if you want a
 * special response structure.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcResult {
    String value();
}
