/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.core.support;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

public class JPATestModule extends AbstractModule {
    private static final String H2_MEMORY_DATABASE = "jdbc:h2:mem:ickstream";
    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<EntityManager>();
    private static EntityManagerFactory emFactory;

    @Override
    protected void configure() {
        System.out.println("Initializing...");
    }

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() throws ClassNotFoundException {
        if (emFactory == null) {
            Class.forName("org.h2.Driver");
            Properties properties = new Properties();
            properties.put("hibernate.connection.url", H2_MEMORY_DATABASE);
            properties.put("hibernate.connection.driver_class", "org.h2.Driver");
            properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
            properties.put("hibernate.show_sql", "true");
            emFactory = Persistence.createEntityManagerFactory("ickstream", properties);
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
