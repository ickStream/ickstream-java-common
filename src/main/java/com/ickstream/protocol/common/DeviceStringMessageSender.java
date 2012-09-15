/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.common;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;

import java.io.UnsupportedEncodingException;

public class DeviceStringMessageSender implements MessageSender {
    private String deviceId;
    private com.ickstream.common.ickdiscovery.MessageSender messageSender;
    private MessageLogger messageLogger;

    public DeviceStringMessageSender(String deviceId, com.ickstream.common.ickdiscovery.MessageSender messageSender) {
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
        try {
            messageSender.sendMessage(deviceId,message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // Just ignore, all platforms we support need to support UTF-8
            e.printStackTrace();
        }
    }
}
