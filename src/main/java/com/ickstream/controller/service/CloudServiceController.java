/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.protocol.service.content.HttpContentService;

public class CloudServiceController extends AbstractServiceController {
    private Service service;

    public CloudServiceController(final Service service, String accessToken, MessageLogger messageLogger) {
        super(new HttpContentService(service.getId(), service.getUrl(), accessToken), service);
        contentService.setMessageLogger(messageLogger);
        this.service = service;
    }

    public void setAccessToken(String accessToken) {
        ((HttpContentService) contentService).setAccessToken(accessToken);
    }
}
