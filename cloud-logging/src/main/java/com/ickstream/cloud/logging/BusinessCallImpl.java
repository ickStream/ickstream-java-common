/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import com.ickstream.protocol.backend.common.BusinessCall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BusinessCallImpl implements BusinessCall {
    protected String service;
    protected String deviceId;
    protected String deviceModel;
    protected String userId;
    protected String address;
    protected Date timestamp;
    protected String method;
    protected long duration;
    protected List<Parameter> parameters = new ArrayList<Parameter>();

    public static class Parameter {
        protected String name;
        protected Object value;

        protected Parameter() {
        }

        public Parameter(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }

    protected BusinessCallImpl() {
    }

    public BusinessCallImpl(String service, String deviceId, String deviceModel, String userId, String address, String method) {
        this.service = service;
        this.deviceId = deviceId;
        this.deviceModel = deviceModel;
        this.userId = userId;
        this.address = address;
        this.timestamp = new Date();
        this.method = method;
    }

    public BusinessCallImpl(String service, String deviceId, String deviceModel, String userId, String address, String method, List<Parameter> parameters) {
        this.service = service;
        this.deviceId = deviceId;
        this.deviceModel = deviceModel;
        this.userId = userId;
        this.address = address;
        this.timestamp = new Date();
        this.method = method;
        if (parameters != null) {
            this.parameters.addAll(parameters);
        }
    }

    public void addParameter(String name, Object value) {
        if (value != null) {
            parameters.add(new Parameter(name, value));
        }
    }

    public void addParameters(Map<String, String> parameterMap) {
        for (Map.Entry<String, String> entry : parameterMap.entrySet()) {
            if (entry.getValue() != null) {
                parameters.add(new Parameter(entry.getKey(), entry.getValue()));
            }
        }
    }

    public void refreshTimestamp() {
        this.timestamp = new Date();
    }

    public void refreshDuration() {
        this.duration = new Date().getTime() - this.timestamp.getTime();
    }

    String getService() {
        return service;
    }

    String getDeviceId() {
        return deviceId;
    }

    String getDeviceModel() {
        return deviceModel;
    }

    String getUserId() {
        return userId;
    }

    String getAddress() {
        return address;
    }

    String getMethod() {
        return method;
    }

    long getDuration() {
        return duration;
    }
}
