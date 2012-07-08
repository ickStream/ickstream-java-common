/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.common.jsonrpc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpJsonRpcService extends AbstractJsonRpcService {

    public <I, T extends I> HttpJsonRpcService(T serviceImplementation, Class<I> serviceInterface) {
        super(serviceImplementation, serviceInterface, true);
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json-rpc");

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