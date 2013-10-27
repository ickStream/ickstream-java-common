/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.core;

import java.util.ArrayList;
import java.util.List;

public class GetUserResponse {
    private String id;
    private String name;
    private String country;
    private List<UserIdentityResponse> identities = new ArrayList<UserIdentityResponse>();

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<UserIdentityResponse> getIdentities() {
        return identities;
    }

    public void setIdentities(List<UserIdentityResponse> identities) {
        this.identities = identities;
    }
}
