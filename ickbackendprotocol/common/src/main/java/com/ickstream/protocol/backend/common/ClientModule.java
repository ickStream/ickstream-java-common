/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ClientModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public Client createClient() {
        return ClientBuilder.newClient();
    }
}
