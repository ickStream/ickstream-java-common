/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.Singleton;
import com.ickstream.common.jsonrpc.HttpJsonRpcService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Abstract implementation of a servlet exposing a specific {@link CloudService} implementation over JSON-RPC
 * Except for handling the JSON-RPC protocol and call the corresponding Java methods, this class will also ensure that
 * {@link RequestContext} is instantiated and filled with appropriate data so it later can be used by the sub class in
 * method calls.
 * <p/>
 * The presumption is that a {@link AbstractOAuthAuthorizationFilter} implementation is executed as a filter before
 * this servlet. If this isn't the case, another filter have to be executed that does the same thing as
 * {@link AbstractOAuthAuthorizationFilter} because it assumes that
 * {@link javax.servlet.http.HttpServletRequest#getRemoteUser()} is filled with the device identity and that
 * {@link javax.servlet.http.HttpServletRequest#getUserPrincipal()} is filled with the user identity.
 */
@Singleton
public abstract class AbstractCloudServlet extends HttpServlet {
    Class<? extends CloudService> implementationClass;
    Class remoteInterface;
    Class userRemoteInterface;

    private static final Pattern LOCAL_ADDRESS_PATTERN = Pattern.compile("(^127\\.0\\..*)|" +
            "(^10\\..*)|" +
            "(^172\\.1[6-9]\\..*)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\..*)|" +
            "(^192\\.168\\..*)");

    /**
     * Creates a new instance of the service with methods that can be accessed from a device
     *
     * @param implementationClass The implementation class of the service implementing {@link CloudService} interface
     * @param remoteInterface     The interface representing the methods which teh service want to make available
     */
    public <I extends CloudService, T extends I> AbstractCloudServlet(Class<T> implementationClass, Class<I> remoteInterface) {
        this.implementationClass = implementationClass;
        this.remoteInterface = remoteInterface;
    }

    /**
     * Creates a new instance of the service with methods that can be accessed both from a device and from an application
     * not represented as a device
     *
     * @param implementationClass The implementation class of the service implementing {@link CloudService} interface
     * @param remoteInterface     The interface representing the methods which the service want to make available from a device
     * @param userRemoteInterface The interface representing the methods which the service want to make available from an application not represented as a device
     */
    public <U extends CloudService, I extends U, T extends I> AbstractCloudServlet(Class<T> implementationClass, Class<I> remoteInterface, Class<U> userRemoteInterface) {
        this(implementationClass, remoteInterface);
        this.userRemoteInterface = userRemoteInterface;
    }

    /**
     * Implements POST requests by parsing them as JSON-RPC and delegating the implementation of the individual methods
     * to the service implementation class provided in the constructor.
     * This implementation will also instantiate a {@link @RequestContext} which can be injected and accessed
     *
     * @param req  The http request object
     * @param resp The http response object
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            RequestContext context = InjectHelper.instance(RequestContext.class);
            String remoteAddr = req.getHeader("X-Forwarded-For");
            if (remoteAddr == null) {
                remoteAddr = req.getRemoteAddr();
            }
            if (remoteAddr != null) {
                remoteAddr = remoteAddr.split(",")[0].trim();
                if (!LOCAL_ADDRESS_PATTERN.matcher(remoteAddr).matches()) {
                    context.setDeviceAddress(remoteAddr);
                }
            }
            context.setContextUrl(req.getScheme() + "://" + req.getServerName() + (req.getServerPort() != 80 ? ":" + req.getServerPort() : "") + req.getContextPath());
            CloudService service = InjectHelper.instance(implementationClass);
            String deviceId = req.getRemoteUser();
            if (deviceId != null && remoteInterface != null) {
                context.setDeviceId(deviceId);
                new HttpJsonRpcService(service, remoteInterface).handle(req, resp);
            } else if (req.getUserPrincipal() != null && userRemoteInterface != null) {
                context.setUserId(req.getUserPrincipal().getName());
                new HttpJsonRpcService(service, userRemoteInterface).handle(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization failed");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
