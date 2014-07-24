/*
 * Copyright (C) 2014 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation that represent a method parameter which should be filled by {@link StreamJsonRpcService} or one of its
 * implementations when a JSON-RPC request/notification is received. This needs to be applied to either a parameter of
 * type {@link com.fasterxml.jackson.databind.JsonNode} or a parameter with a Java bean which contains set/get methods
 * for each attribute that can be handled.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonRpcParamArray {
    /**
     * @return true if the parameter is optional, else false
     */
    boolean optional() default false;
}
