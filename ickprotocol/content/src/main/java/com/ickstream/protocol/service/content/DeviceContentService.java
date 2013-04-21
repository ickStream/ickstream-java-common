/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.ickdiscovery.MessageSender;
import com.ickstream.common.ickdiscovery.ServiceType;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.DeviceStringMessageSender;

public class DeviceContentService extends ContentService {
    public DeviceContentService(String id, MessageSender messageSender) {
        super(id, new DeviceStringMessageSender(id, ServiceType.SERVICE, messageSender));
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((DeviceStringMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }
}
