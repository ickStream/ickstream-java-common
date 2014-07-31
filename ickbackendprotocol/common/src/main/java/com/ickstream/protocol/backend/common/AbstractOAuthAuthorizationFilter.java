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

/**
 * Abstract filter class which handles authentication and rejects any requests from unauthorized users.
 */
public abstract class AbstractOAuthAuthorizationFilter implements Filter {
    private FilterConfig config;
    private CoreBackendService coreBackendService;
    private static final Pattern OAUTH_AUTHORIZATION_PATTERN = Pattern.compile("\\s*(\\w*)\\s+(.*)");

    /**
     * Returns the {@link CoreBackendService} client implementation this service want to use.
     * Typically the sub class should use the {@link com.ickstream.protocol.service.corebackend.CoreBackendServiceFactory} class to implement this.
     *
     * @return The CoreBackendService client instance that should be used
     */
    protected abstract CoreBackendService getCoreBackendService();

    protected static class OAuthServletRequest extends HttpServletRequestWrapper {
        private OAuthMessage oauthMessage;
        private CoreBackendService coreBackendService;
        private Boolean authorized;
        private String deviceId;
        private String userId;
        private String otherId;

        public OAuthServletRequest(HttpServletRequest request, OAuthMessage oauthMessage, CoreBackendService coreBackendService) {
            super(request);
            this.oauthMessage = oauthMessage;
            this.coreBackendService = coreBackendService;
        }

        public CoreBackendService getCoreBackendService() {
            return coreBackendService;
        }

        @Override
        public String getRemoteUser() {
            return deviceId;
        }

        @Override
        public Principal getUserPrincipal() {
            return new Principal() {
                public String getName() {
                    return userId != null ? userId : otherId;
                }
            };
        }

        /**
         * Retrieves information about the device based on a device access token.
         *
         * @param deviceAccessToken The device access token
         * @return Information about the device
         */
        protected DeviceResponse getDeviceForToken(String deviceAccessToken) {
            return coreBackendService.getDeviceByToken(deviceAccessToken);
        }

        /**
         * Retrieves information about the user based on a user access token.
         *
         * @param userAccessToken The user access token
         * @return Information about the user
         */
        protected UserResponse getUserForToken(String userAccessToken) {
            return coreBackendService.getUserByToken(userAccessToken);
        }

        /**
         * Retrieves other identity based on an authorization token
         * By default this method returns null, so it must be overridden in sub classes if
         * to enable usage of additional types of authorization tokens
         *
         * @param accessToken The authorization token
         * @return An object identity or null if the token couldn't be resolved to an authorized object
         */
        protected String getOtherForToken(String accessToken) {
            return null;
        }

        /**
         * Implements {@link HttpServletRequest#isUserInRole(String)} by accessing the {@link CoreBackendService} through
         * the client provided by the {@link #getCoreBackendService} method
         *
         * @param role The role to check
         * @return true if the authenticated device/user/application has the specified role
         */
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
                                String otherId = getOtherForToken(accessToken);
                                if (otherId != null) {
                                    this.otherId = otherId;
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

    /**
     * Initialize the filter and instantiate the {@link CoreBackendService} client if not already created by calling
     * {@link #getCoreBackendService()} method
     *
     * @param filterConfig The filter configuration
     */
    public void init(FilterConfig filterConfig) {
        this.config = filterConfig;
        if (coreBackendService == null) {
            coreBackendService = getCoreBackendService();
        }
    }

    /**
     * Creates a new {@link OAuthServletRequest} instance
     *
     * @param req     The http request
     * @param message The {@link OAuthMessage} representing the request
     * @return A newly created {@link OAuthServletRequest} instance
     */
    protected OAuthServletRequest createRequest(HttpServletRequest req, OAuthMessage message) {
        return new OAuthServletRequest(req, message, coreBackendService);
    }

    /**
     * Apply the filter by verifying that the user, device or application specified in the OAuth Authorization header
     * have the permission to access the requested method. If permissions aren't fulfilled, this methods returns {@link HttpServletResponse#SC_UNAUTHORIZED} without
     * dispatching the call further in the filter chain.
     *
     * @param request     The servlet request
     * @param response    The servlet response
     * @param filterChain The filter chain
     * @throws IOException
     * @throws ServletException
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        OAuthMessage message = new OAuthMessage(req.getMethod(), req.getRequestURL().toString(), getParameters(req), req.getInputStream());
        HttpServletRequest wrapper = createRequest(req, message);
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

    /**
     * Performs some clean-up of internal resources
     */
    public void destroy() {
        coreBackendService = null;
    }
}
