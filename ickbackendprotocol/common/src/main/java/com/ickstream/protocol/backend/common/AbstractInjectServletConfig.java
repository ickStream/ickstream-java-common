/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInjectServletConfig extends GuiceServletContextListener {
    private String serviceId;

    protected AbstractInjectServletConfig() {
    }

    protected AbstractInjectServletConfig(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    protected Injector getInjector() {
        List<Module> modules = new ArrayList<Module>();
        modules.add(createInjectModule());
        if (System.getProperty("ickstream-logging", "true").equalsIgnoreCase("true") &&
                (serviceId == null || System.getProperty("ickstream-" + serviceId + "-logging", "true").equalsIgnoreCase("true"))) {
            Module module = createBusinessLoggingModule();
            if (module != null) {
                modules.add(module);
            }
        } else {
            modules.add(new NoBusinessLoggerModule());
        }

        if (System.getProperty("ickstream-cache", "true").equalsIgnoreCase("true") &&
                (serviceId == null || System.getProperty("ickstream-" + serviceId + "-cache", "true").equalsIgnoreCase("true"))) {
            Module module = createCacheManagerModule();
            if (module != null) {
                modules.add(module);
            }
        } else {
            modules.add(new NoCacheManagerModule());
        }
        modules.addAll(createAdditionalInjectModules());
        Injector injector = Guice.createInjector(modules);
        InjectHelper.setInjector(injector);
        return injector;
    }

    protected abstract ServletModule createInjectModule();

    protected Module createBusinessLoggingModule() {
        return null;
    }

    protected Module createCacheManagerModule() {
        return new NoCacheManagerModule();
    }

    protected List<Module> createAdditionalInjectModules() {
        return new ArrayList<Module>();
    }
}
