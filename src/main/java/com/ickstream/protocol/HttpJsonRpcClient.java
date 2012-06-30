/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import com.ickstream.protocol.cloud.ServerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpJsonRpcClient extends JsonRpcClient {
    private String endpoint;
    private HttpClient client;

    public HttpJsonRpcClient(HttpClient client, String endpoint) {
        this.client = client;
        this.endpoint = endpoint;
    }

    @Override
    public <T> T callMethod(String method, Object parameters, Class<T> responseClass) throws ServerException {
        JsonRpcRequest request = new JsonRpcRequest();
        request.setMethod(method);
        request.setId(getNextId());
        if (parameters != null) {
            request.setParams(getJsonHelper().objectToJson(parameters));
        }
        try {
            String requestString = getJsonHelper().objectToString(request);
            System.out.println("SENDING Cloud REQUEST (" + endpoint + "):\n" + requestString + "\n");
            HttpClient httpclient = client;
            HttpPost httpRequest = new HttpPost(endpoint);
            httpRequest.setEntity(new StringEntity(requestString));
            httpRequest.setHeader("Authorization", "OAuth " + getAccessToken());
            HttpResponse httpResponse = httpclient.execute(httpRequest);
            String responseString = EntityUtils.toString(httpResponse.getEntity());
            JsonRpcResponse response = getJsonHelper().stringToObject(responseString, JsonRpcResponse.class);
            System.out.println("RECEIVING Cloud RESPONSE:\n" + getJsonHelper().objectToString(response) + "\n");
            if (response.getError() == null) {
                if (responseClass != null) {
                    return getJsonHelper().jsonToObject(response.getResult(), responseClass);
                } else {
                    return null;
                }
            } else {
                String code = response.getError().get("code").getTextValue();
                String message = response.getError().get("message").getTextValue();
                throw new ServerException(code, message);
            }
        } catch (IOException e) {
            throw new RuntimeException("Cloud server access error", e);
        }

    }
}
