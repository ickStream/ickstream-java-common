/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.corebackend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ServiceResponse implements Serializable {
    private String id;
    private String name;
    private String url;
    private String apiKey;
    private String apiSecret;
    private String type;
    private String addServiceUrl;
    private String customData1;
    private String customData2;
    private String customData3;
    private String customData4;
    private Map<String, Map<String, String>> attributes = new HashMap<String, Map<String, String>>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getAddServiceUrl() {
        return addServiceUrl;
    }

    public void setAddServiceUrl(String addServiceUrl) {
        this.addServiceUrl = addServiceUrl;
    }

    public String getCustomData1() {
        return customData1;
    }

    public void setCustomData1(String customData1) {
        this.customData1 = customData1;
    }

    public String getCustomData2() {
        return customData2;
    }

    public void setCustomData2(String customData2) {
        this.customData2 = customData2;
    }

    public String getCustomData3() {
        return customData3;
    }

    public void setCustomData3(String customData3) {
        this.customData3 = customData3;
    }

    public String getCustomData4() {
        return customData4;
    }

    public void setCustomData4(String customData4) {
        this.customData4 = customData4;
    }

    public Map<String, Map<String, String>> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Map<String, String>> attributes) {
        this.attributes = attributes;
    }
}
