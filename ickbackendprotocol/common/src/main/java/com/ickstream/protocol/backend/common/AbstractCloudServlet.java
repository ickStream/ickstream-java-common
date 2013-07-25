/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
            CloudService service = InjectHelper.instance(implementationClass);
            String deviceId = req.getRemoteUser();
            if (deviceId != null && remoteInterface != null) {
                RequestContext context = InjectHelper.instance(RequestContext.class);
                context.setUserId(req.getUserPrincipal().getName());
                context.setDeviceId(deviceId);
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
                new HttpJsonRpcService(service, remoteInterface).handle(req, resp);
            } else if (userRemoteInterface != null) {
                RequestContext context = InjectHelper.instance(RequestContext.class);
                context.setUserId(req.getUserPrincipal() != null ? req.getUserPrincipal().getName() : null);
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
