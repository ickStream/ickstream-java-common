/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class JsonRpcClient {
    private Integer id = 1;
    private String endpoint;
    private Client client;

    public JsonRpcClient(Client client, String endpoint) {
        this.client = client;
        this.endpoint = endpoint;
    }

    public void setAccessToken(final String accessToken) {
        client.removeAllFilters();
        client.addFilter(new ClientFilter() {
            @Override
            public ClientResponse handle(ClientRequest clientRequest) throws ClientHandlerException {
                clientRequest.getHeaders().add("Authorization", "OAuth " + accessToken);
                return getNext().handle(clientRequest);
            }
        });
    }

    public <T> T callMethod(String method, Object parameters, Class<T> responseClass) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        JsonRpcRequest request = new JsonRpcRequest();
        request.setMethod(method);
        request.setId(id++);
        if (parameters != null) {
            request.setParams(mapper.valueToTree(parameters));
        }
        try {
            String requestString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.valueToTree(request));
            System.out.println("SENDING Cloud REQUEST (" + endpoint + "):\n" + requestString + "\n");
            String responseString = client.resource(endpoint).type(MediaType.APPLICATION_JSON_TYPE).post(String.class, requestString);
            JsonRpcResponse response = mapper.treeToValue(mapper.readTree(responseString), JsonRpcResponse.class);
            System.out.println("RECEIVING Cloud RESPONSE:\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response) + "\n");
            if (response.getError() == null) {
                if (responseClass != null) {
                    return mapper.treeToValue(response.getResult(), responseClass);
                } else {
                    return null;
                }
            } else {
                String code = response.getError().get("code").getTextValue();
                String message = response.getError().get("message").getTextValue();
                throw new RuntimeException("Cloud server error: " + code + ": " + message);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cloud server access error", e);
        }

    }
}
