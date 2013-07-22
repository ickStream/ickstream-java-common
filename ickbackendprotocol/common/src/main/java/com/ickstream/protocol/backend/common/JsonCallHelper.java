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

import javax.ws.rs.client.Invocation;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Map;

public class JsonCallHelper {
    public static JsonNode getJsonForResource(BusinessCall businessCall, String url, String authorizationHeader) throws IOException {
        return getJsonForResource(businessCall, url, authorizationHeader, null);
    }

    public static JsonNode getJsonForResource(String url, String authorizationHeader) throws IOException {
        return getJsonForResource(url, authorizationHeader, null);
    }

    public static JsonNode getJsonForPostResource(BusinessCall businessCall, String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPostResource(businessCall, url, authorizationHeader, body, null, context);
    }

    public static JsonNode getJsonForPostResource(String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPostResource(url, authorizationHeader, body, null, context);
    }

    public static JsonNode getJsonForPostResource(String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPostResource(null, url, authorizationHeader, body, headers, context);
    }

    public static JsonNode getJsonForPostResource(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPostResource(businessCall, url, null, authorizationHeader, body, headers, context);
    }

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

    public static JsonNode getJsonForPutResource(BusinessCall businessCall, String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPutResource(businessCall, url, authorizationHeader, body, null, context);
    }

    public static JsonNode getJsonForPutResource(String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForPutResource(url, authorizationHeader, body, null, context);
    }

    public static JsonNode getJsonForPutResource(String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPutResource(null, url, authorizationHeader, body, headers, context);
    }

    public static JsonNode getJsonForPutResource(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPutResource(businessCall, url, null, authorizationHeader, body, headers, context);
    }

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

    public static JsonNode getJsonForDeleteResource(BusinessCall businessCall, String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForDeleteResource(businessCall, url, authorizationHeader, body, null, context);
    }

    public static JsonNode getJsonForDeleteResource(String url, String authorizationHeader, String body, String context) throws IOException {
        return getJsonForDeleteResource(url, authorizationHeader, body, null, context);
    }

    public static JsonNode getJsonForDeleteResource(String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForDeleteResource(null, url, authorizationHeader, body, headers, context);
    }

    public static JsonNode getJsonForDeleteResource(BusinessCall businessCall, String url, String authorizationHeader, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForDeleteResource(businessCall, url, null, authorizationHeader, body, headers, context);
    }

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

    public static JsonNode getJsonForResource(String url, String authorizationHeader, String context) throws IOException {
        return getJsonForResource(null, url, authorizationHeader, context);
    }

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

    public static String getAuthorizationHeader(final UserServiceResponse userService) {
        return getAuthorizationHeader("Bearer", userService);
    }

    public static String getAuthorizationHeader(final String tokenType, final UserServiceResponse userService) {
        return tokenType + " " + userService.getAccessToken();
    }

    public static Invocation.Builder authorized(Invocation.Builder builder, final UserServiceResponse userService) {
        return authorized(builder, "Bearer", userService);
    }

    public static Invocation.Builder authorized(Invocation.Builder builder, final String tokenType, final UserServiceResponse userService) {
        return builder.header("Authorization", getAuthorizationHeader(tokenType, userService));
    }
}
