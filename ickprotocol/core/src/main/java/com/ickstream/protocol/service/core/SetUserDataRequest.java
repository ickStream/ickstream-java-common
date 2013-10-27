/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

public class SetUserDataRequest {
    private String name;
    private String country;

    public SetUserDataRequest() {
    }

    public SetUserDataRequest(String name) {
        this.name = name;
    }

    public SetUserDataRequest(String name, String country) {
        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
