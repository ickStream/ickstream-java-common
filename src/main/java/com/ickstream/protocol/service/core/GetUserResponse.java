/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.service.core;

import java.util.ArrayList;
import java.util.List;

public class GetUserResponse {
    private String id;
    private String name;
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

    public List<UserIdentityResponse> getIdentities() {
        return identities;
    }

    public void setIdentities(List<UserIdentityResponse> identities) {
        this.identities = identities;
    }
}
