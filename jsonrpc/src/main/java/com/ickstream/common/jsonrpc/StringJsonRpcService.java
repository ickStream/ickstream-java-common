/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.WriterOutputStream;

import java.io.StringWriter;

/**
 * Implementation of a JSON-RPC service, the purpose is to abstract JSON-RPC transport protocol and parsing from the
 * service implementation.
 * <p/>
 * The communication is handled as {@link String} objects. If the communication channel is already using
 * {@link java.io.InputStream} and {@link java.io.OutputStream} it's preferred to use the {@link StreamJsonRpcService} instead of this
 * class
 * <p/>
 * See {@link StreamJsonRpcService} for more details
 */
public class StringJsonRpcService extends StreamJsonRpcService {
    /**
     * See {@link StreamJsonRpcService#StreamJsonRpcService(Object, Class)} for more information
     */
    public <I, T extends I> StringJsonRpcService(T serviceImplementation, Class<I> serviceInterface) {
        super(serviceImplementation, serviceInterface);
    }

    /**
     * See {@link StreamJsonRpcService#StreamJsonRpcService(Object, Class, Boolean)} for more information
     */
    public <I, T extends I> StringJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid) {
        super(serviceImplementation, serviceInterface, returnOnVoid);
    }

    /**
     * See {@link StreamJsonRpcService#StreamJsonRpcService(Object, Class, Boolean, Boolean)} for more information
     */
    public <I, T extends I> StringJsonRpcService(T serviceImplementation, Class<I> serviceInterface, Boolean returnOnVoid, Boolean ignoreResponses) {
        super(serviceImplementation, serviceInterface, returnOnVoid, ignoreResponses);
    }

    /**
     * Process a JSON-RPC request received as a {@link String} and produce the response as a {@link String}, it's the
     * caller responsibility to read/write the strings to the appropriate communication channel
     *
     * @param request The JSON-RPC request/notification as a string
     * @return The JSON-RPC response as a String or null if no response is generated
     */
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
