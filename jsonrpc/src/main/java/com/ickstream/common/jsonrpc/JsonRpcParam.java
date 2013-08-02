/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation representing a method parameter which should be filled by {@link StreamJsonRpcService} or one of its
 * implementations when a JSON-RPC request/notification is received
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcParam {
    /**
     * @return The name of the parameter
     */
    String name();

    /**
     * @return true if the parameter is optional, else false
     */
    boolean optional() default false;
}
