/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.oauth;

import org.scribe.model.OAuthConstants;

public abstract class DefaultApi20 extends org.scribe.builder.api.DefaultApi20 {
    public String getClientIdParameterName() {
        return OAuthConstants.CLIENT_ID;
    }

    public String getClientSecretParameterName() {
        return OAuthConstants.CLIENT_SECRET;
    }

    public String getTokenParameterName() {
        return null;
    }
}
