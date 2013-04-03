/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BusinessLoggerEntry extends BusinessCallImpl {
    protected String error;
    protected String exception;
    protected Map<String, Object> parameters = new HashMap<String, Object>();

    public BusinessLoggerEntry() {
    }

    public BusinessLoggerEntry(String service, String deviceId, String deviceModel, String userId, String address, String method, Map<String, Object> parameters, String error, String exception, Long duration, Long timestamp) {
        super(service, deviceId, deviceModel, userId, address, method, null);
        setDuration(duration);
        setTimestamp(timestamp);
        if (parameters != null) {
            this.parameters.putAll(new HashMap<String, Object>(parameters));
        }
        this.error = error;
        this.exception = exception;
    }

    @Override
    public void addParameter(String name, Object value) {
        parameters.put(name, value);
    }

    @Override
    public void addParameters(Map<String, String> parameterMap) {
        parameters.putAll(parameterMap);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public String getService() {
        return service;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public String getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public Long getTimestamp() {
        return timestamp.getTime();
    }

    public String getMethod() {
        return method;
    }

    public long getDuration() {
        return duration;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = new Date(timestamp);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
