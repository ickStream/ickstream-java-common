/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation which is used to specify which JSON-RPC errors that can be produced by a JSON-RPC service.
 * The annotation is used by {@link StreamJsonRpcService} or one of its implementations to create the appropriate
 * JSON-RPC response message after an exception has been thrown
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcErrors {
    JsonRpcError[] value();
}
