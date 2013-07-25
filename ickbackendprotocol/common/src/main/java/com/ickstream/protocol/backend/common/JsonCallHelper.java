/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.common.jsonrpc.JsonHelper;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Helper class to make it easy to access JSON services provided over HTTP
 */
public class JsonCallHelper {
    /**
     * See {@link #getJsonForPostResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForResource(BusinessCall businessCall, String url, String authorizationHeader) throws IOException {
        return getJsonForResource(businessCall, url, authorizationHeader, null);
    }

    /**
     * See {@link #getJsonForPostResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForResource(String url, String authorizationHeader) throws IOException {
        return getJsonForResource(url, authorizationHeader, null);
    }

    /**
     * See {@link #getJsonForPostResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPostResource(BusinessCall businessCall, String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPostResource(businessCall, url, authorizationHeader, body, null, context);
    }

    /**
     * See {@link #getJsonForPostResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPostResource(String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPostResource(url, authorizationHeader, body, null, context);
    }

    /**
     * See {@link #getJsonForPostResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPostResource(String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPostResource(null, url, authorizationHeader, body, headers, context);
    }

    /**
     * See {@link #getJsonForPostResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPostResource(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPostResource(businessCall, url, null, authorizationHeader, body, headers, context);
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
     * @param context             A JSON path to a sub result within the returned result that should extracted instead of returning the complete result
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static JsonNode getJsonForPostResource(BusinessCall businessCall, String url, String mediaType, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
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
                InputStream stream = response.getEntity().getContent();
                JsonNode jsonResult = new JsonHelper().streamToObject(stream, JsonNode.class);
                if (context != null) {
                    String[] contexts = context.split("\\.");
                    for (String subContext : contexts) {
                        if (jsonResult.has(subContext)) {
                            jsonResult = jsonResult.get(subContext);
                        } else {
                            return null;
                        }
                    }
                }
                return jsonResult;
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
     * See {@link #getJsonForPutResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPutResource(BusinessCall businessCall, String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPutResource(businessCall, url, authorizationHeader, body, null, context);
    }

    /**
     * See {@link #getJsonForPutResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPutResource(String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPutResource(url, authorizationHeader, body, null, context);
    }

    /**
     * See {@link #getJsonForPutResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPutResource(String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPutResource(null, url, authorizationHeader, body, headers, context);
    }

    /**
     * See {@link #getJsonForPutResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForPutResource(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPutResource(businessCall, url, null, authorizationHeader, body, headers, context);
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
     * @param context             A JSON path to a sub result within the returned result that should extracted instead of returning the complete result
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static JsonNode getJsonForPutResource(BusinessCall businessCall, String url, String mediaType, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
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
                InputStream stream = response.getEntity().getContent();
                JsonNode jsonResult = new JsonHelper().streamToObject(stream, JsonNode.class);
                if (context != null) {
                    String[] contexts = context.split("\\.");
                    for (String subContext : contexts) {
                        if (jsonResult.has(subContext)) {
                            jsonResult = jsonResult.get(subContext);
                        } else {
                            return null;
                        }
                    }
                }
                return jsonResult;
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
     * See {@link #getJsonForDeleteResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForDeleteResource(BusinessCall businessCall, String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForDeleteResource(businessCall, url, authorizationHeader, body, null, context);
    }

    /**
     * See {@link #getJsonForDeleteResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForDeleteResource(String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForDeleteResource(url, authorizationHeader, body, null, context);
    }

    /**
     * See {@link #getJsonForDeleteResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForDeleteResource(String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForDeleteResource(null, url, authorizationHeader, body, headers, context);
    }

    /**
     * See {@link #getJsonForDeleteResource(BusinessCall, String, String, String, String, java.util.Map, String)}
     */
    public static JsonNode getJsonForDeleteResource(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForDeleteResource(businessCall, url, null, authorizationHeader, body, headers, context);
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
     * @param context             A JSON path to a sub result within the returned result that should extracted instead of returning the complete result
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static JsonNode getJsonForDeleteResource(BusinessCall businessCall, String url, String mediaType, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
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
                InputStream stream = response.getEntity().getContent();
                JsonNode jsonResult = new JsonHelper().streamToObject(stream, JsonNode.class);
                if (context != null) {
                    String[] contexts = context.split("\\.");
                    for (String subContext : contexts) {
                        if (jsonResult.has(subContext)) {
                            jsonResult = jsonResult.get(subContext);
                        } else {
                            return null;
                        }
                    }
                }
                return jsonResult;
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
     * See {@link #getJsonForResource(BusinessCall, String, String, String)}
     */
    public static JsonNode getJsonForResource(String url, String authorizationHeader, String context) throws IOException {
        return getJsonForResource(null, url, authorizationHeader, context);
    }

    /**
     * Makes a HTTP GET request and parses and returns the response as a {@link JsonNode} object
     *
     * @param businessCall        The {@link BusinessCall} object to use when business logging, this method will call {@link com.ickstream.protocol.backend.common.BusinessCall#refreshTimestamp()} before the request and {@link BusinessCall#refreshDuration()} after the request
     * @param url                 The URL to make the request to, it will be resolved through {@link UriResolver} by this method
     * @param authorizationHeader The Authorization header to when making the HTTP request
     * @param context             A JSON path to a sub result within the returned result that should extracted instead of returning the complete result
     * @return A {@link JsonNode} object representing the result
     * @throws IOException If the call fails, either locally or remotely
     */
    public static JsonNode getJsonForResource(BusinessCall businessCall, String url, String authorizationHeader, String context) throws IOException {
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
                InputStream stream = response.getEntity().getContent();
                JsonNode jsonResult = new JsonHelper().streamToObject(stream, JsonNode.class);
                if (context != null) {
                    String[] contexts = context.split("\\.");
                    for (String subContext : contexts) {
                        if (jsonResult.has(subContext)) {
                            jsonResult = jsonResult.get(subContext);
                        } else {
                            return null;
                        }
                    }
                }
                return jsonResult;
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
