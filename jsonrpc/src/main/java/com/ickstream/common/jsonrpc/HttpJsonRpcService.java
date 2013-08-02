/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.common.jsonrpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implementation of a JSON-RPC service, the purpose is to abstract JSON-RPC transport protocol and parsing from the
 * service implementation.
 * <p/>
 * The communication is handled as {@link HttpServletRequest} and {@link HttpServletResponse} objects.
 * <p/>
 * See {@link StreamJsonRpcService} for more details
 */
public class HttpJsonRpcService extends StreamJsonRpcService {

    /**
     * See {@link StreamJsonRpcService#StreamJsonRpcService(Object, Class)} for more information
     */
    public <I, T extends I> HttpJsonRpcService(T serviceImplementation, Class<I> serviceInterface) {
        super(serviceImplementation, serviceInterface, true);
    }

    /**
     * Process a JSON-RPC request received as a {@link HttpServletRequest} representing a HTTP POST request and writes
     * the result (if any) to the specified {@link HttpServletResponse}.
     *
     * @param request  The {@link HttpServletRequest} that contains the JSON-RPC request/notification
     * @param response The {@link HttpServletResponse} which the result should be written to
     */
    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        InputStream input = null;
        OutputStream output = response.getOutputStream();

        if (request.getMethod().equals("POST")) {
            input = request.getInputStream();
        } else {
            throw new IOException(
                    "Invalid request method, only POST is supported");
        }
        super.handle(input, output);
    }

}