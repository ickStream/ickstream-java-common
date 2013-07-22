/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.corebackend.ApplicationResponse;
import com.ickstream.protocol.service.corebackend.CoreBackendService;
import com.ickstream.protocol.service.corebackend.DeviceResponse;
import com.ickstream.protocol.service.corebackend.UserResponse;
import net.oauth.OAuth;
import net.oauth.OAuthMessage;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractOAuthAuthorizationFilter implements Filter {
    private FilterConfig config;
    private CoreBackendService coreBackendService;
    private static final Pattern OAUTH_AUTHORIZATION_PATTERN = Pattern.compile("\\s*(\\w*)\\s+(.*)");

    protected abstract CoreBackendService getCoreBackendService();

    public static class OAuthServletRequest extends HttpServletRequestWrapper {
        private OAuthMessage oauthMessage;
        private CoreBackendService coreBackendService;
        private Boolean authorized;
        private String deviceId;
        private String userId;
        private String applicationId;

        public OAuthServletRequest(HttpServletRequest request, OAuthMessage oauthMessage, CoreBackendService coreBackendService) {
            super(request);
            this.oauthMessage = oauthMessage;
            this.coreBackendService = coreBackendService;
        }

        @Override
        public String getRemoteUser() {
            return deviceId;
        }

        @Override
        public Principal getUserPrincipal() {
            return new Principal() {
                public String getName() {
                    return userId != null ? userId : applicationId;
                }
            };
        }

        protected CoreBackendService getCoreBackendService() {
            return coreBackendService;
        }

        protected DeviceResponse getDeviceForToken(String accessToken) {
            return coreBackendService.getDeviceByToken(accessToken);
        }

        protected UserResponse getUserForToken(String accessToken) {
            return coreBackendService.getUserByToken(accessToken);
        }

        protected ApplicationResponse getApplicationForToken(String accessToken) {
            return null;
        }

        @Override
        public boolean isUserInRole(String role) {
            if (!role.equals("user")) {
                return false;
            }

            if (authorized == null) {
                authorized = false;
                try {
                    String accessToken = oauthMessage.getToken();
                    if (accessToken != null) {
                        DeviceResponse device = getDeviceForToken(accessToken);
                        if (device != null) {
                            this.deviceId = device.getId();
                            this.userId = device.getUserId();
                            authorized = true;
                        } else {
                            UserResponse user = getUserForToken(accessToken);
                            if (user != null) {
                                userId = user.getId();
                                authorized = true;
                            } else {
                                ApplicationResponse application = getApplicationForToken(accessToken);
                                if (application != null && application.getActive() != null && application.getActive()) {
                                    applicationId = application.getId();
                                    authorized = true;
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            return authorized;
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        if (coreBackendService == null) {
            coreBackendService = getCoreBackendService();
        }
    }

    protected OAuthServletRequest createRequest(HttpServletRequest req, OAuthMessage message, CoreBackendService coreBackendService) {
        return new OAuthServletRequest(req, message, coreBackendService);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        OAuthMessage message = new OAuthMessage(req.getMethod(), req.getRequestURL().toString(), getParameters(req), req.getInputStream());
        HttpServletRequest wrapper = createRequest(req, message, coreBackendService);
        if (wrapper.isUserInRole("user")) {
            filterChain.doFilter(wrapper, response);
        } else {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization failed");
        }
    }

    private static List<OAuth.Parameter> getParameters(HttpServletRequest request) {
        List<OAuth.Parameter> list = new ArrayList<OAuth.Parameter>();

        for (Enumeration<String> headers = request.getHeaders("Authorization"); headers != null
                && headers.hasMoreElements(); ) {
            String header = headers.nextElement();
            for (OAuth.Parameter parameter : OAuthMessage
                    .decodeAuthorization(header)) {
                if (!"realm".equalsIgnoreCase(parameter.getKey())) {
                    list.add(parameter);
                }
            }
            if (list.size() == 0 && header != null) {
                Matcher m = OAUTH_AUTHORIZATION_PATTERN.matcher(header);
                if (m.find()) {
                    if ("OAuth".equalsIgnoreCase(m.group(1)) || "Bearer".equalsIgnoreCase(m.group(1))) {
                        list.add(new OAuth.Parameter(OAuth.OAUTH_TOKEN, m.group(2)));
                    }
                }
            }
        }
        for (Object e : request.getParameterMap().entrySet()) {
            Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) e;
            String name = entry.getKey();
            for (String value : entry.getValue()) {
                list.add(new OAuth.Parameter(name, value));
            }
        }
        return list;
    }

    public void destroy() {
    }
}
