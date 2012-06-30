/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.cloud.content;

import com.ickstream.protocol.DeviceJsonRpcClient;
import com.ickstream.protocol.JsonRpcResponseHandler;
import com.ickstream.protocol.device.MessageSender;

public class DeviceContentService extends ContentService {
    public DeviceContentService(String id, MessageSender messageSender) {
        super(id,new DeviceJsonRpcClient(id, messageSender));
    }

    public DeviceContentService(String id, String accessToken, MessageSender messageSender) {
        super(id,new DeviceJsonRpcClient(id,messageSender),accessToken);
    }

    public JsonRpcResponseHandler getJsonRpcResponseHandler() {
        return ((DeviceJsonRpcClient)getJsonRpcClient());
    }
}
