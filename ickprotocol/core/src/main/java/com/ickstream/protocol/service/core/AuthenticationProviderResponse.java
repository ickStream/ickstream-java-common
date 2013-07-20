/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class AuthenticationProviderResponse {
    private String id;
    private String name;
    private String icon;
    private String url;
    private String addIdentityUrl;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddIdentityUrl() {
        return addIdentityUrl;
    }

    public void setAddIdentityUrl(String addIdentityUrl) {
        this.addIdentityUrl = addIdentityUrl;
    }
}
