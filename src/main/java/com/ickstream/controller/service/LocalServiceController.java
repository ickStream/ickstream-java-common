/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.service;

import com.ickstream.common.ickdiscovery.MessageSender;
import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.service.ServiceInformation;
import com.ickstream.protocol.service.content.DeviceContentService;

public class LocalServiceController extends AbstractServiceController {

    public LocalServiceController(final Service service, MessageSender messageSender, MessageLogger messageLogger) {
        super(new DeviceContentService(service.getId(), messageSender), service);
        contentService.setMessageLogger(messageLogger);

        contentService.getServiceInformation(new MessageHandlerAdapter<ServiceInformation>() {
            @Override
            public void onMessage(ServiceInformation message) {
                service.setServiceUrl(message.getServiceUrl());
            }
        }, 5000);
    }

}
