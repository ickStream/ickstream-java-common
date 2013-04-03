/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.HashMap;
import java.util.Map;

public class TestScope implements Scope {
    private String context = "DEFAULT";

    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            private Map<String, T> cachedObjects = new HashMap<String, T>();

            @Override
            public T get() {
                if (!cachedObjects.containsKey(context)) {
                    cachedObjects.put(context, unscoped.get());
                }
                return cachedObjects.get(context);
            }
        };
    }
}
