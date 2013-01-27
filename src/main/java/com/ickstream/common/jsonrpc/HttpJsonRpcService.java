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

public class HttpJsonRpcService extends StreamJsonRpcService {

    public <I, T extends I> HttpJsonRpcService(T serviceImplementation, Class<I> serviceInterface) {
        super(serviceImplementation, serviceInterface, true);
    }

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