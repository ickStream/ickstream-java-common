/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.protocol.HttpJsonRpcClient;
import org.apache.http.client.HttpClient;

public class HttpContentService extends ContentService {

    public HttpContentService(HttpClient client, String id, String endpoint) {
        super(id,new HttpJsonRpcClient(client, endpoint));
    }

    public HttpContentService(HttpClient client, String id, String endpoint, String accessToken) {
        super(id,new HttpJsonRpcClient(client, endpoint),accessToken);
    }

}
