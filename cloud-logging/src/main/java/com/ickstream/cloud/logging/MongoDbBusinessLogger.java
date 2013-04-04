/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.protocol.backend.common.BusinessCall;
import com.ickstream.protocol.backend.common.BusinessLogger;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

public class MongoDbBusinessLogger implements BusinessLogger, BusinessLoggerQuery {
    private DB db;

    public MongoDbBusinessLogger(DB db) {
        this.db = db;
    }

    private BasicDBObject createLogEntry(BusinessCallImpl businessCall, String error, Throwable exception) {
        BasicDBObject entry = new BasicDBObject();
        entry.append("timestamp", businessCall.timestamp);
        if (businessCall.getDuration() > 0) {
            entry.append("duration", businessCall.getDuration());
        } else {
            entry.append("duration", new Date().getTime() - businessCall.timestamp.getTime());
        }
        entry.append("service", businessCall.getService());
        if (businessCall.getDeviceId() != null) {
            entry.append("deviceId", businessCall.getDeviceId());
        }
        if (businessCall.getDeviceModel() != null) {
            entry.append("deviceModel", businessCall.getDeviceModel());
        }
        if (businessCall.getUserId() != null) {
            entry.append("userId", businessCall.getUserId());
        }
        if (businessCall.getAddress() != null) {
            entry.append("address", businessCall.getAddress());
        }
        entry.append("method", businessCall.getMethod());
        if (businessCall.parameters != null && businessCall.parameters.size() > 0) {
            BasicDBObject parameters = new BasicDBObject();
            for (BusinessCallImpl.Parameter parameter : businessCall.parameters) {
                if (parameter.value instanceof JsonNode) {
                    parameters.append(parameter.name, JSON.parse(parameter.value.toString()));
                } else {
                    parameters.append(parameter.name, parameter.value);
                }
            }
            entry.append("parameters", parameters);
        }
        if (error != null) {
            entry.append("error", error);
        } else if (exception != null) {
            entry.append("error", exception.getMessage());
        }
        if (exception != null) {
            StringWriter exceptionString = new StringWriter();
            exception.printStackTrace(new PrintWriter(exceptionString));
            entry.append("exception", exceptionString.toString());
        }
        return entry;
    }

    @Override
    public void logSuccessful(BusinessCall businessCall) {
        db.getCollection("businesslog").insert(createLogEntry((BusinessCallImpl) businessCall, null, null));
    }

    @Override
    public void logFailed(BusinessCall businessCall, String error, Throwable exception) {
        db.getCollection("businesslog").insert(createLogEntry((BusinessCallImpl) businessCall, error, exception));
    }

    @Override
    public void logFailed(BusinessCall businessCall, Throwable exception) {
        db.getCollection("businesslog").insert(createLogEntry((BusinessCallImpl) businessCall, null, exception));
    }

    @Override
    public void logFailed(BusinessCall businessCall, String error) {
        db.getCollection("businesslog").insert(createLogEntry((BusinessCallImpl) businessCall, error, null));
    }

    @Override
    public List<BusinessLoggerEntry> find(String userId, String excludeUserId, Boolean excludeAnonymousAccess, String deviceId, String serviceId, String servicePrefix, String servicePostfix, String excludeServicePrefix, String excludeServicePostfix, String method, Long minimumDuration, Date beforeTimestamp, Boolean onlyErrors, Integer count) {
        List<BasicDBObject> criterias = new ArrayList<BasicDBObject>();
        if (userId != null && userId.length() > 0) {
            criterias.add(new BasicDBObject("userId", userId));
        }
        if (excludeUserId != null && excludeUserId.length() > 0) {
            criterias.add(new BasicDBObject("userId", new BasicDBObject("$ne", excludeUserId)));
        }
        if (excludeAnonymousAccess != null && excludeAnonymousAccess) {
            criterias.add(new BasicDBObject("userId", new BasicDBObject("$ne", null)));
        }
        if (deviceId != null && deviceId.length() > 0) {
            criterias.add(new BasicDBObject("deviceId", deviceId));
        }
        if (serviceId != null && serviceId.length() > 0) {
            criterias.add(new BasicDBObject("service", serviceId));
        }
        if (servicePrefix != null && servicePrefix.length() > 0) {
            criterias.add(new BasicDBObject("service", java.util.regex.Pattern.compile("^" + servicePrefix)));
        }
        if (servicePostfix != null && servicePostfix.length() > 0) {
            criterias.add(new BasicDBObject("service", java.util.regex.Pattern.compile(servicePostfix + "$")));
        }
        if (excludeServicePrefix != null && excludeServicePrefix.length() > 0) {
            criterias.add(new BasicDBObject("service", new BasicDBObject("$not", java.util.regex.Pattern.compile("^" + excludeServicePrefix))));
        }
        if (excludeServicePostfix != null && excludeServicePostfix.length() > 0) {
            criterias.add(new BasicDBObject("service", new BasicDBObject("$not", java.util.regex.Pattern.compile(excludeServicePostfix + "$"))));
        }
        if (method != null && method.length() > 0) {
            criterias.add(new BasicDBObject("method", method));
        }
        if (minimumDuration != null) {
            criterias.add(new BasicDBObject("duration", new BasicDBObject("$gt", minimumDuration)));
        }
        if (beforeTimestamp != null) {
            criterias.add(new BasicDBObject("timestamp", new BasicDBObject("$lt", beforeTimestamp)));
        }
        if (onlyErrors != null && onlyErrors) {
            criterias.add(new BasicDBObject("error", new BasicDBObject("$ne", null)));
        }
        BasicDBObject query;
        if (criterias.size() > 1) {
            query = new BasicDBObject("$and", criterias);
        } else if (criterias.size() > 0) {
            query = criterias.get(0);
        } else {
            query = new BasicDBObject();
        }
        DBCursor cursor = db.getCollection("businesslog").find(query).sort(new BasicDBObject("timestamp", -1));
        try {
            List<BusinessLoggerEntry> result = new ArrayList<BusinessLoggerEntry>(count != null ? count : 100);
            while (cursor.hasNext() && (count == null || count > result.size())) {
                DBObject obj = cursor.next();
                Map<String, Object> parameters = new HashMap<String, Object>();
                Object paramObj = obj.get("parameters");
                if (paramObj instanceof Map) {
                    Map paramMap = (Map) paramObj;
                    for (Object key : paramMap.keySet()) {
                        parameters.put(key.toString(), paramMap.get(key));
                    }
                }

                BusinessLoggerEntry entry = new BusinessLoggerEntry(
                        obj.get("service") != null ? obj.get("service").toString() : null,
                        obj.get("deviceId") != null ? obj.get("deviceId").toString() : null,
                        obj.get("deviceModel") != null ? obj.get("deviceModel").toString() : null,
                        obj.get("userId") != null ? obj.get("userId").toString() : null,
                        obj.get("address") != null ? obj.get("address").toString() : null,
                        obj.get("method") != null ? obj.get("method").toString() : null,
                        parameters,
                        obj.get("error") != null ? obj.get("error").toString() : null,
                        obj.get("exception") != null ? obj.get("exception").toString() : null,
                        obj.get("duration") != null ? (Long) obj.get("duration") : null,
                        obj.get("timestamp") != null ? ((Date) obj.get("timestamp")).getTime() : null
                );
                result.add(entry);
            }
            return result;
        } finally {
            cursor.close();
        }
    }
}
