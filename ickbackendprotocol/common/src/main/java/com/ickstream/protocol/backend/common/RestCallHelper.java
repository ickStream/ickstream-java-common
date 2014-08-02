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

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.protocol.service.corebackend.UserServiceResponse;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Helper class to make it easy to access REST services provided over HTTP
 */
public class RestCallHelper {

    /**
     * See {@link #callResourceWithPost(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPost(BusinessCall businessCall, String url, String authorizationHeader, String body) throws IOException {
        return callResourceWithPost(businessCall, url, authorizationHeader, body, null);
    }

    /**
     * See {@link #callResourceWithPost(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPost(String url, String authorizationHeader, String body) throws IOException {
        return callResourceWithPost(url, authorizationHeader, body, null);
    }

    /**
     * See {@link #callResourceWithPost(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPost(String url, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        return callResourceWithPost(null, url, authorizationHeader, body, headers);
    }

    /**
     * See {@link #callResourceWithPost(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPost(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        return callResourceWithPost(businessCall, url, null, authorizationHeader, body, headers);
    }

    /**
     * Makes a HTTP POST request and parses and returns the response as a {@link JsonNode} object
     *
     * @param businessCall        The {@link BusinessCall} object to use when business logging, this method will call {@link com.ickstream.protocol.backend.common.BusinessCall#refreshTimestamp()} before the request and {@link BusinessCall#refreshDuration()} after the request
     * @param url                 The URL to make the request to, it will be resolved through {@link UriResolver} by this method
     * @param mediaType           The media type for the body content, or null if no specific media type is needed
     * @param authorizationHeader The Authorization header to when making the HTTP request
     * @param body                The body to use in the HTTP request
     * @param headers             Additional headers to use in the HTTP request
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static String callResourceWithPost(BusinessCall businessCall, String url, String mediaType, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        HttpPost request;
        try {
            request = new HttpPost(InjectHelper.instance(UriResolver.class).resolve(url));
        } catch (URISyntaxException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.setHeader(entry.getKey(), entry.getValue());
            }
        }
        if (authorizationHeader != null) {
            request.addHeader("Authorization", authorizationHeader);
        }
        try {
            HttpResponse response;
            if (body != null) {
                if (mediaType != null) {
                    request.setEntity(new StringEntity(body, ContentType.create(mediaType, "UTF-8")));
                } else {
                    request.setEntity(new StringEntity(body));
                }
            }

            if (businessCall != null) {
                businessCall.refreshTimestamp();
            }

            response = InjectHelper.instance(HttpClient.class).execute(request);

            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity());
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN || response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException("Http request failed: " + response.getStatusLine().getStatusCode() + (response.getStatusLine().getReasonPhrase() != null ? response.getStatusLine().getReasonPhrase() : ""));
            }
        } catch (IOException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
    }

    /**
     * See {@link #callResourceWithPut(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPut(BusinessCall businessCall, String url, String authorizationHeader, String body) throws IOException {
        return callResourceWithPut(businessCall, url, authorizationHeader, body, null);
    }

    /**
     * See {@link #callResourceWithPut(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPut(String url, String authorizationHeader, String body) throws IOException {
        return callResourceWithPut(url, authorizationHeader, body, null);
    }

    /**
     * See {@link #callResourceWithPut(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPut(String url, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        return callResourceWithPut(null, url, authorizationHeader, body, headers);
    }

    /**
     * See {@link #callResourceWithPut(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithPut(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        return callResourceWithPut(businessCall, url, null, authorizationHeader, body, headers);
    }

    /**
     * Makes a HTTP PUT request and parses and returns the response as a {@link JsonNode} object
     *
     * @param businessCall        The {@link BusinessCall} object to use when business logging, this method will call {@link com.ickstream.protocol.backend.common.BusinessCall#refreshTimestamp()} before the request and {@link BusinessCall#refreshDuration()} after the request
     * @param url                 The URL to make the request to, it will be resolved through {@link UriResolver} by this method
     * @param mediaType           The media type for the body content, or null if no specific media type is needed
     * @param authorizationHeader The Authorization header to when making the HTTP request
     * @param body                The body to use in the HTTP request
     * @param headers             Additional headers to use in the HTTP request
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static String callResourceWithPut(BusinessCall businessCall, String url, String mediaType, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        HttpPut request;
        try {
            request = new HttpPut(InjectHelper.instance(UriResolver.class).resolve(url));
        } catch (URISyntaxException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (authorizationHeader != null) {
            request.addHeader("Authorization", authorizationHeader);
        }
        try {
            HttpResponse response;
            if (body != null) {
                if (mediaType != null) {
                    request.setEntity(new StringEntity(body, ContentType.create(mediaType)));
                } else {
                    request.setEntity(new StringEntity(body));
                }
            }

            if (businessCall != null) {
                businessCall.refreshTimestamp();
            }

            response = InjectHelper.instance(HttpClient.class).execute(request);

            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity());
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN || response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException("Http request failed: " + response.getStatusLine().getStatusCode() + (response.getStatusLine().getReasonPhrase() != null ? response.getStatusLine().getReasonPhrase() : ""));
            }
        } catch (IOException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
    }

    /**
     * See {@link #callResourceWithDelete(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithDelete(BusinessCall businessCall, String url, String authorizationHeader, String body) throws IOException {
        return callResourceWithDelete(businessCall, url, authorizationHeader, body, null);
    }

    /**
     * See {@link #callResourceWithDelete(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithDelete(String url, String authorizationHeader, String body) throws IOException {
        return callResourceWithDelete(url, authorizationHeader, body, null);
    }

    /**
     * See {@link #callResourceWithDelete(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithDelete(String url, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        return callResourceWithDelete(null, url, authorizationHeader, body, headers);
    }

    /**
     * See {@link #callResourceWithDelete(BusinessCall, String, String, String, String, java.util.Map)}
     */
    public static String callResourceWithDelete(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        return callResourceWithDelete(businessCall, url, null, authorizationHeader, body, headers);
    }

    /**
     * Makes a HTTP DELETE request and parses and returns the response as a {@link JsonNode} object
     *
     * @param businessCall        The {@link BusinessCall} object to use when business logging, this method will call {@link com.ickstream.protocol.backend.common.BusinessCall#refreshTimestamp()} before the request and {@link BusinessCall#refreshDuration()} after the request
     * @param url                 The URL to make the request to, it will be resolved through {@link UriResolver} by this method
     * @param mediaType           The media type for the body content, or null if no specific media type is needed
     * @param authorizationHeader The Authorization header to when making the HTTP request
     * @param body                The body to use in the HTTP request
     * @param headers             Additional headers to use in the HTTP request
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static String callResourceWithDelete(BusinessCall businessCall, String url, String mediaType, String authorizationHeader, String body, Map<String, String> headers) throws IOException {
        HttpDelete request;
        try {
            request = new HttpDelete(InjectHelper.instance(UriResolver.class).resolve(url));
        } catch (URISyntaxException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        if (authorizationHeader != null) {
            request.addHeader("Authorization", authorizationHeader);
        }
        try {
            HttpResponse response;
            if (body != null) {
                // TODO:!!!
            }

            if (businessCall != null) {
                businessCall.refreshTimestamp();
            }

            response = InjectHelper.instance(HttpClient.class).execute(request);

            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity());
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN || response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException("Http request failed: " + response.getStatusLine().getStatusCode() + (response.getStatusLine().getReasonPhrase() != null ? response.getStatusLine().getReasonPhrase() : ""));
            }
        } catch (IOException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
    }

    /**
     * See {@link #callResourceWithGet(BusinessCall, String, String)}
     */
    public static String callResourceWithGet(String url, String authorizationHeader) throws IOException {
        return callResourceWithGet(null, url, authorizationHeader);
    }

    /**
     * Makes a HTTP GET request and parses and returns the response as a {@link JsonNode} object
     *
     * @param businessCall        The {@link BusinessCall} object to use when business logging, this method will call {@link com.ickstream.protocol.backend.common.BusinessCall#refreshTimestamp()} before the request and {@link BusinessCall#refreshDuration()} after the request
     * @param url                 The URL to make the request to, it will be resolved through {@link UriResolver} by this method
     * @param authorizationHeader The Authorization header to when making the HTTP request
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static String callResourceWithGet(BusinessCall businessCall, String url, String authorizationHeader) throws IOException {
        try {
            HttpGet request;
            try {
                request = new HttpGet(InjectHelper.instance(UriResolver.class).resolve(url));
            } catch (URISyntaxException e) {
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
                throw new IOException(e);
            }
            if (authorizationHeader != null) {
                request.addHeader("Authorization", authorizationHeader);
            }

            HttpResponse response;

            if (businessCall != null) {
                businessCall.refreshTimestamp();
            }

            response = InjectHelper.instance(HttpClient.class).execute(request);

            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK && response.getEntity() != null) {
                return EntityUtils.toString(response.getEntity());
            } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN || response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException("Http request failed: " + response.getStatusLine().getStatusCode() + (response.getStatusLine().getReasonPhrase() != null ? response.getStatusLine().getReasonPhrase() : ""));
            }
        } catch (IOException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
    }

    /**
     * Return the Authorization header to use for the specified service, "Bearer" will be used for the token in the header
     *
     * @param userService The service to base Authorization header on
     * @return A complete Authorization header value for the specified service
     */
    public static String getAuthorizationHeader(final UserServiceResponse userService) {
        return getAuthorizationHeader("Bearer", userService);
    }

    /**
     * Return the Authorization header to use for the specified service
     *
     * @param tokenType   The token type to use in the Authorization header
     * @param userService The service to base Authorization header on
     * @return A complete Authorization header value for the specified service
     */
    public static String getAuthorizationHeader(final String tokenType, final UserServiceResponse userService) {
        return tokenType + " " + userService.getAccessToken();
    }
}
