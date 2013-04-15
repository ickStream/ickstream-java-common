/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ickstream.protocol.service.corebackend.UserServiceResponse;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.filter.ClientFilter;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;

public class JsonCallHelper {
    public static JsonNode getJsonForResource(Client client, BusinessCall businessCall, String url) throws IOException {
        return getJsonForResource(client, businessCall, url, null);
    }

    public static JsonNode getJsonForResource(Client client, String url) throws IOException {
        return getJsonForResource(client, url, null);
    }

    public static JsonNode getJsonForPostResource(Client client, BusinessCall businessCall, String url, String body, String context) throws IOException {
        return getJsonForPostResource(client, businessCall, url, body, null, context);
    }

    public static JsonNode getJsonForPostResource(Client client, String url, String body, String context) throws IOException {
        return getJsonForPostResource(client, url, body, null, context);
    }

    public static JsonNode getJsonForPostResource(Client client, String url, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPostResource(client, null, url, body, headers, context);
    }

    public static JsonNode getJsonForPostResource(Client client, BusinessCall businessCall, String url, String body, Map<String, String> headers, String context) throws IOException {
        WebResource.Builder builder = client.resource(url).type(MediaType.APPLICATION_FORM_URLENCODED);
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
                queryResult = builder.post(String.class, body);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            } else {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                queryResult = builder.post(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            }
            JsonNode jsonResult = new ObjectMapper().readTree(queryResult);
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
        } catch (UniformInterfaceException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == ClientResponse.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        }
    }

    public static JsonNode getJsonForPutResource(Client client, BusinessCall businessCall, String url, String body, String context) throws IOException {
        return getJsonForPutResource(client, businessCall, url, body, null, context);
    }

    public static JsonNode getJsonForPutResource(Client client, String url, String body, String context) throws IOException {
        return getJsonForPutResource(client, url, body, null, context);
    }

    public static JsonNode getJsonForPutResource(Client client, String url, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForPutResource(client, null, url, body, headers, context);
    }

    public static JsonNode getJsonForPutResource(Client client, BusinessCall businessCall, String url, String body, Map<String, String> headers, String context) throws IOException {
        WebResource.Builder builder = client.resource(url).type(MediaType.APPLICATION_FORM_URLENCODED);
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
                queryResult = builder.put(String.class, body);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            } else {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                queryResult = builder.put(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            }
            JsonNode jsonResult = new ObjectMapper().readTree(queryResult);
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
        } catch (UniformInterfaceException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == ClientResponse.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        }
    }

    public static JsonNode getJsonForDeleteResource(Client client, BusinessCall businessCall, String url, String body, String context) throws IOException {
        return getJsonForDeleteResource(client, businessCall, url, body, null, context);
    }

    public static JsonNode getJsonForDeleteResource(Client client, String url, String body, String context) throws IOException {
        return getJsonForDeleteResource(client, url, body, null, context);
    }

    public static JsonNode getJsonForDeleteResource(Client client, String url, String body, Map<String, String> headers, String context) throws IOException {
        return getJsonForDeleteResource(client, null, url, body, headers, context);
    }

    public static JsonNode getJsonForDeleteResource(Client client, BusinessCall businessCall, String url, String body, Map<String, String> headers, String context) throws IOException {
        WebResource.Builder builder = client.resource(url).type(MediaType.APPLICATION_FORM_URLENCODED);
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
                queryResult = builder.delete(String.class, body);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            } else {
                if (businessCall != null) {
                    businessCall.refreshTimestamp();
                }
                queryResult = builder.delete(String.class);
                if (businessCall != null) {
                    businessCall.refreshDuration();
                }
            }
            JsonNode jsonResult = new ObjectMapper().readTree(queryResult);
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
        } catch (UniformInterfaceException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == ClientResponse.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        }
    }

    public static JsonNode getJsonForResource(Client client, String url, String context) throws IOException {
        return getJsonForResource(client, null, url, context);
    }

    public static JsonNode getJsonForResource(Client client, BusinessCall businessCall, String url, String context) throws IOException {
        try {
            WebResource webResource = client.resource(url);
            if (businessCall != null) {
                businessCall.refreshTimestamp();
            }
            String queryResult = webResource.get(String.class);
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            JsonNode jsonResult = new ObjectMapper().readTree(queryResult);
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
        } catch (UniformInterfaceException e) {
            if (businessCall != null) {
                businessCall.refreshDuration();
            }
            if (e.getResponse().getStatus() == ClientResponse.Status.UNAUTHORIZED.getStatusCode()) {
                throw new UnauthorizedAccessException();
            } else {
                throw new IOException(e);
            }
        }
    }

    public static void addAuthorizationHeader(Client client, final UserServiceResponse userService) {
        addAuthorizationHeader(client, "Bearer", userService);
    }

    public static void addAuthorizationHeader(Client client, final String tokenType, final UserServiceResponse userService) {
        client.addFilter(new ClientFilter() {
            @Override
            public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
                clientRequest.getHeaders().add("Authorization", tokenType + " " + userService.getAccessToken());
                return getNext().handle(clientRequest);
            }
        });
    }
}
