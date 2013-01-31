/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Service {
    private String id;
    private String name;
    private String url;
    private String serviceUrl;

    public Service(String id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public Service(String id, String name, String url, String serviceUrl) {
        this(id, name, url);
        this.serviceUrl = serviceUrl;
    }

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

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Service)) {
            return false;
        }
        return new EqualsBuilder().append(id, ((Service) o).getId()).isEquals();
    }
}
