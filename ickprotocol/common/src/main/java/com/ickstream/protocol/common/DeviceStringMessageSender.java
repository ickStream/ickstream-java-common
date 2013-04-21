/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common;

import com.ickstream.common.ickdiscovery.ServiceType;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;

import java.io.UnsupportedEncodingException;

public class DeviceStringMessageSender implements MessageSender {
    private String deviceId;
    private ServiceType serviceType;
    private com.ickstream.common.ickdiscovery.MessageSender messageSender;
    private MessageLogger messageLogger;

    public DeviceStringMessageSender(String deviceId, ServiceType serviceType, com.ickstream.common.ickdiscovery.MessageSender messageSender) {
        this.deviceId = deviceId;
        this.serviceType = serviceType;
        this.messageSender = messageSender;
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    @Override
    public void sendMessage(String message) {
        if (messageLogger != null) {
            messageLogger.onOutgoingMessage(deviceId, message);
        }
        try {
            if(serviceType != null) {
                messageSender.sendMessage(deviceId, serviceType, message.getBytes("UTF-8"));
            }else {
                messageSender.sendMessage(deviceId, message.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            // Just ignore, all platforms we support need to support UTF-8
            e.printStackTrace();
        }
    }
}
