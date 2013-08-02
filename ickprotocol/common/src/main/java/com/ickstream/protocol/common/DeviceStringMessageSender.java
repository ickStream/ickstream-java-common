/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.common;

import com.ickstream.common.ickdiscovery.ServiceType;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;

import java.io.UnsupportedEncodingException;

/**
 * Message sender which communicates using ickStream P2P protocol, this message sender
 * should be used when communicating with devices or services on the local network.
 */
public class DeviceStringMessageSender implements MessageSender {
    private String deviceId;
    private ServiceType serviceType;
    private com.ickstream.common.ickdiscovery.MessageSender messageSender;
    private MessageLogger messageLogger;

    /**
     * Creates a new instance
     *
     * @param deviceId      The device identity to send messages to
     * @param serviceType   The type of device to send messages to
     * @param messageSender The {@link com.ickstream.common.ickdiscovery.MessageSender} implementation to use for sending messages
     */
    public DeviceStringMessageSender(String deviceId, ServiceType serviceType, com.ickstream.common.ickdiscovery.MessageSender messageSender) {
        this.deviceId = deviceId;
        this.serviceType = serviceType;
        this.messageSender = messageSender;
    }

    /**
     * Set message logger to be used to log outgoing messages
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    /**
     * Sends an outgoing message using the messages sender specified in the constructor
     *
     * @param message The message to send
     */
    @Override
    public void sendMessage(String message) {
        if (messageLogger != null) {
            messageLogger.onOutgoingMessage(deviceId, message);
        }
        try {
            if (serviceType != null) {
                messageSender.sendMessage(deviceId, serviceType, message.getBytes("UTF-8"));
            } else {
                messageSender.sendMessage(deviceId, message.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            // Just ignore, all platforms we support need to support UTF-8
            e.printStackTrace();
        }
    }
}
