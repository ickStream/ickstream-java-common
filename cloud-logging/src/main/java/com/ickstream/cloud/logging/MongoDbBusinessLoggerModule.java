/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.ickstream.protocol.backend.common.BusinessLogger;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;

import java.net.UnknownHostException;

public class MongoDbBusinessLoggerModule extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    @Named("ickstream-cloud-logging")
    @Singleton
    public DB createDB() {
        try {
            Mongo mongo = new Mongo();
            DB db = mongo.getDB("ickstream-cloud-logging");
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("timestamp", 1));
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("duration", 1));
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("timestamp", 1).append("duration", 1));
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("service", 1).append("timestamp", 1));
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("service", 1).append("method", 1).append("timestamp", 1));
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("userId", 1).append("timestamp", 1));
            db.getCollection("businesslog").ensureIndex(new BasicDBObject("deviceId", 1).append("timestamp", 1));
            return db;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    public BusinessLogger createBusinessLogger(@Named("ickstream-cloud-logging") DB db) {
        return new MongoDbBusinessLogger(db);
    }

    @Provides
    @Singleton
    public BusinessLoggerQuery createBusinessLoggerQuery(@Named("ickstream-cloud-logging") DB db) {
        return new MongoDbBusinessLogger(db);
    }
}
