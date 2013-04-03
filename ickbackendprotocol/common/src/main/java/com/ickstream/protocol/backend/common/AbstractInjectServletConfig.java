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
    @Override
    protected Injector getInjector() {
        List<Module> modules = new ArrayList<Module>();
        modules.add(createInjectModule());
        Module module = createBusinessLoggingModule();
        if (module != null) {
            modules.add(createBusinessLoggingModule());
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

    protected List<Module> createAdditionalInjectModules() {
        return new ArrayList<Module>();
    }
}
