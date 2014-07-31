/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.protocol.common;

import com.ickstream.common.ickp2p.IckP2pException;
import com.ickstream.common.ickp2p.ServiceType;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;

import java.io.UnsupportedEncodingException;

/**
 * Message sender which communicates using ickStream P2P protocol, this message sender
 * should be used when communicating with devices or services on the local network.
 */
public class DeviceStringMessageSender implements MessageSender {
    private ServiceType fromServiceType;
    private String deviceId;
    private ServiceType serviceType;
    private com.ickstream.common.ickp2p.MessageSender messageSender;
    private MessageLogger messageLogger;

    /**
     * Creates a new instance
     *
     * @param fromServiceType The type of service which makes the call
     * @param deviceId      The device identity to send messages to
     * @param serviceType   The type of device to send messages to
     * @param messageSender The {@link com.ickstream.common.ickp2p.MessageSender} implementation to use for sending messages
     */
    public DeviceStringMessageSender(ServiceType fromServiceType, String deviceId, ServiceType serviceType, com.ickstream.common.ickp2p.MessageSender messageSender) {
        this.fromServiceType = fromServiceType;
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
            messageSender.sendMsg(deviceId, serviceType, fromServiceType, message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // Just ignore, all platforms we support need to support UTF-8
            e.printStackTrace();
        } catch (IckP2pException e) {
            // TODO: Feels like we should handle this somehow
            e.printStackTrace();
        }
    }
}
