/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

public class InjectHelper {
    private static Injector injector = null;
    private static List<AbstractModule> modules = null;

    public static List<AbstractModule> getModules() {
        if (modules == null) {
            modules = new ArrayList<AbstractModule>();
            Iterator<AbstractModule> moduleIterator = ServiceLoader.load(AbstractModule.class).iterator();
            while (moduleIterator.hasNext()) {
                modules.add(moduleIterator.next());
            }
        }
        return modules;
    }

    public static void setInjector(Injector newInjector) {
        injector = newInjector;
    }

    public static void injectMembers(Object obj) {
        if (injector == null) {
            throw new RuntimeException("Injector framework not initialized yet");
        }
        injector.injectMembers(obj);
    }

    public static <T> T instanceWithName(Class<T> T, String name) {
        if (injector == null) {
            throw new RuntimeException("Injector framework not initialized yet");
        }
        return (T) injector.getInstance(Key.get(T, Names.named(name)));
    }

    public static <T> T instance(Class<T> T) {
        if (injector == null) {
            throw new RuntimeException("Injector framework not initialized yet");
        }
        return (T) injector.getInstance(Key.get(T));
    }
}
