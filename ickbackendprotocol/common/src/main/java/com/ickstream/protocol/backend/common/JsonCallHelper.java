/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.common.jsonrpc.JsonHelper;
import com.ickstream.protocol.service.corebackend.UserServiceResponse;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
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
        Invocation.Builder builder = null;
        try {
            builder = InjectHelper.instance(Client.class).target(InjectHelper.instance(UriResolver.class).resolve(url)).request();
        } catch (URISyntaxException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }
        try {
            String queryResult;
            if (body != null) {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                if (mediaType != null) {
                    queryResult = builder.buildPost(Entity.entity(body, mediaType)).invoke(String.class);
                } else {
                    queryResult = builder.buildPost(Entity.text(body)).invoke(String.class);
                }
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            } else {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                queryResult = builder.buildPost(null).invoke(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            }
            JsonNode jsonResult = new JsonHelper().stringToObject(queryResult, JsonNode.class);
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
        } catch (WebApplicationException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        } catch (ProcessingException e) {
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
        Invocation.Builder builder = null;
        try {
            builder = InjectHelper.instance(Client.class).target(InjectHelper.instance(UriResolver.class).resolve(url)).request();
        } catch (URISyntaxException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }
        if (authorizationHeader != null) {
            builder = builder.header("Authorization", authorizationHeader);
        }
        try {
            String queryResult;
            if (body != null) {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                if (mediaType != null) {
                    queryResult = builder.buildPut(Entity.entity(body, mediaType)).invoke(String.class);
                } else {
                    queryResult = builder.buildPut(Entity.text(body)).invoke(String.class);
                }
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            } else {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                queryResult = builder.buildPut(null).invoke(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            }
            JsonNode jsonResult = new JsonHelper().stringToObject(queryResult, JsonNode.class);
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
        } catch (WebApplicationException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        } catch (ProcessingException e) {
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
        Invocation.Builder builder = null;
        try {
            builder = InjectHelper.instance(Client.class).target(InjectHelper.instance(UriResolver.class).resolve(url)).request();
        } catch (URISyntaxException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            throw new IOException(e);
        }
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder = builder.header(entry.getKey(), entry.getValue());
            }
        }
        if (authorizationHeader != null) {
            builder = builder.header("Authorization", authorizationHeader);
        }
        try {
            String queryResult;
            if (body != null) {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                //TODO: No body ?
                queryResult = builder.buildDelete().invoke(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            } else {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                queryResult = builder.buildDelete().invoke(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            }
            JsonNode jsonResult = new JsonHelper().stringToObject(queryResult, JsonNode.class);
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
        } catch (WebApplicationException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        } catch (ProcessingException e) {
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
            WebTarget webResource = null;
            try {
                webResource = InjectHelper.instance(Client.class).target(InjectHelper.instance(UriResolver.class).resolve(url));
            } catch (URISyntaxException e) {
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
                throw new IOException(e);
            }
            if (businessCall != null) {
                businessCall.refreshTimestamp();
            }
            String queryResult = null;
            if (authorizationHeader != null) {
                queryResult = webResource.request().header("Authorization", authorizationHeader).get(String.class);
            } else {
                queryResult = webResource.request().get(String.class);
            }
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            JsonNode jsonResult = new JsonHelper().stringToObject(queryResult, JsonNode.class);
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
        } catch (WebApplicationException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        } catch (ProcessingException e) {
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
