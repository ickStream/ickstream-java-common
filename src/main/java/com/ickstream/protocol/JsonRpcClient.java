/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

public class JsonRpcClient {
    private Integer id = 1;
    private String endpoint;
    private HttpClient client;
    private String accessToken;

    public JsonRpcClient(HttpClient client, String endpoint) {
        this.client = client;
        this.endpoint = endpoint;
    }

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
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
            HttpClient httpclient = client;
            HttpPost httpRequest = new HttpPost(endpoint);
            httpRequest.setEntity(new StringEntity(requestString));
            httpRequest.setHeader("Authorization", "OAuth " + accessToken);
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
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
