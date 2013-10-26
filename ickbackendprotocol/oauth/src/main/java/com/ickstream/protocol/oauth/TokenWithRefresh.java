/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.oauth;

import org.scribe.model.Token;

public class TokenWithRefresh extends Token {
    private final String refreshToken;

    public TokenWithRefresh(String token, String secret, String refreshToken) {
        super(token, secret);
        this.refreshToken = refreshToken;
    }

    public TokenWithRefresh(String token, String secret, String refreshToken, String rawResponse) {
        super(token, secret, rawResponse);
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
