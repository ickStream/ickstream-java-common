/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Singleton;
import com.ickstream.common.jsonrpc.HttpJsonRpcService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public abstract class AbstractCloudServlet extends HttpServlet {
    Class<? extends CloudService> implementationClass;
    Class remoteInterface;
    Class userRemoteInterface;

    public <I extends CloudService, T extends I> AbstractCloudServlet(Class<T> implementationClass, Class<I> remoteInterface) {
        this.implementationClass = implementationClass;
        this.remoteInterface = remoteInterface;
    }

    public <U extends CloudService, I extends U, T extends I> AbstractCloudServlet(Class<T> implementationClass, Class<I> remoteInterface, Class<U> userRemoteInterface) {
        this(implementationClass, remoteInterface);
        this.userRemoteInterface = userRemoteInterface;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
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
                    if (!remoteAddr.matches("(^127\\.0\\..*)|" +
                            "(^10\\..*)|" +
                            "(^172\\.1[6-9]\\..*)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\..*)|" +
                            "(^192\\.168\\..*)")) {
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
                    if (!remoteAddr.matches("(^127\\.0\\..*)|" +
                            "(^10\\..*)|" +
                            "(^172\\.1[6-9]\\..*)|(^172\\.2[0-9]\\.)|(^172\\.3[0-1]\\..*)|" +
                            "(^192\\.168\\..*)")) {
                        context.setDeviceAddress(remoteAddr);
                    }
                }
                context.setContextUrl(req.getScheme() + "://" + req.getServerName() + (req.getServerPort() != 80 ? ":" + req.getServerPort() : "") + req.getContextPath());
                new HttpJsonRpcService(service, userRemoteInterface).handle(req, resp);
            } else {
                throw new RuntimeException("Unauthorized access");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new RuntimeException("Not implemented, use POST");
    }
}
