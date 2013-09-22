/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.content;

import com.ickstream.common.ickp2p.MessageSender;
import com.ickstream.common.ickp2p.ServiceType;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.common.DeviceStringMessageSender;

/**
 * An ickstream P2P based client used to communicate with content services on local network using the
 * Content Access protocol.
 */
public class DeviceContentService extends ContentService {
    /**
     * Creates a new instance for the specified service identity using the specified message sender
     *
     * @param fromServiceType The type of service which will be using this client
     * @param id              The service identity to communicate with
     * @param messageSender   The ickstream P2P message sender which should be used to send message to the service
     */
    public DeviceContentService(ServiceType fromServiceType, String id, MessageSender messageSender) {
        super(id, new DeviceStringMessageSender(fromServiceType, id, ServiceType.SERVICE, messageSender));
    }

    /**
     * Set the message logger to use to log outgoing messages
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        ((DeviceStringMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }
}
