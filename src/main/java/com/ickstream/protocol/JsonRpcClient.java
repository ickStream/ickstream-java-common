/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import com.ickstream.protocol.cloud.ServerException;

public abstract class JsonRpcClient {
    private Integer id = 1;
    private String accessToken;
    private JsonHelper jsonHelper = new JsonHelper();

    public void setAccessToken(final String accessToken) {
        this.accessToken = accessToken;
    }

    protected String getAccessToken() {
        return this.accessToken;
    }

    protected Integer getNextId() {
        return id++;
    }

    protected JsonHelper getJsonHelper() {
        return jsonHelper;
    }
    public abstract <T> T callMethod(String method, Object parameters, Class<T> responseClass) throws ServerException;
}
