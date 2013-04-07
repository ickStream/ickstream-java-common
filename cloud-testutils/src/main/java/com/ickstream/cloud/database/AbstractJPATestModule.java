/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

public abstract class AbstractJPATestModule extends AbstractModule {
    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();
    private static EntityManagerFactory emFactory;

    protected abstract String getDatabaseUrl();

    protected abstract String getPersistentUnit();

    protected String getDatabaseDriver() {
        return "org.h2.Driver";
    }

    protected String getDatabaseDialect() {
        return "org.hibernate.dialect.H2Dialect";
    }

    @Override
    protected void configure() {
        System.out.println("Initializing...");
    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() throws ClassNotFoundException {
        if (emFactory == null) {
            Class.forName(getDatabaseDriver());
            Properties properties = new Properties();
            properties.put("hibernate.connection.url", getDatabaseUrl());
            properties.put("hibernate.connection.driver_class", getDatabaseDriver());
            properties.put("hibernate.dialect", getDatabaseDialect());
            properties.put("hibernate.show_sql", "true");
            emFactory = Persistence.createEntityManagerFactory(getPersistentUnit(), properties);
        }
        return emFactory;
    }

    @Provides
    public EntityManager provideEntityManager(EntityManagerFactory emFactory) {
        EntityManager em = ENTITY_MANAGER_CACHE.get();
        if (em == null) {
            ENTITY_MANAGER_CACHE.set(em = emFactory.createEntityManager());
        }
        return em;
    }
}
