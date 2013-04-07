/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.StringWriter;

public class StringJsonRpcService extends StreamJsonRpcService {
    public <I, T extends I> StringJsonRpcService(T serviceImplementation, Class<I> serviceInterface) {
        super(serviceImplementation, serviceInterface);
    }

    public <I, T extends I> StringJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid) {
        super(serviceImplementation, serviceInterface, returnOnVoid);
    }

    public <I, T extends I> StringJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid, Boolean ignoreResponses) {
        super(serviceImplementation, serviceInterface, returnOnVoid, ignoreResponses);
    }

    public String handle(String request) {
        StringWriter writer = new StringWriter();
        super.handle(IOUtils.toInputStream(request), new WriterOutputStream(writer));
        String result = writer.toString();
        if (result != null && result.length() > 0) {
            return result;
        } else {
            return null;
        }
    }
}