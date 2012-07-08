package com.ickstream.protocol;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.common.jsonrpc.MessageSender;

/**
 * Created with IntelliJ IDEA.
 * User: erland
 * Date: 7/7/12
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeviceMessageSender implements MessageSender {
    private String deviceId;
    private com.ickstream.protocol.device.MessageSender messageSender;
    private MessageLogger messageLogger;

    public DeviceMessageSender(String deviceId, com.ickstream.protocol.device.MessageSender messageSender) {
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
