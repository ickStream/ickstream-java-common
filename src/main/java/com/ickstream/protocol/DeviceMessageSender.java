/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;

public class DeviceMessageSender implements MessageSender {
    private String deviceId;
    private com.ickstream.common.ickdiscovery.MessageSender messageSender;
    private MessageLogger messageLogger;

    public DeviceMessageSender(String deviceId, com.ickstream.common.ickdiscovery.MessageSender messageSender) {
        this.deviceId = deviceId;
        this.messageSender = messageSender;
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    @Override
    public void sendMessage(String message) {
        if(messageLogger != null) {
            messageLogger.onOutgoingMessage(deviceId, message);
        }
        messageSender.sendMessage(deviceId,message);
    }
}
