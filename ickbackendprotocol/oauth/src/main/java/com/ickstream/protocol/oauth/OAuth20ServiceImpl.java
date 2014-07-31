/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.protocol.oauth;

import com.ickstream.protocol.backend.common.UnauthorizedAccessException;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

import java.io.UnsupportedEncodingException;

public class OAuth20ServiceImpl extends org.scribe.oauth.OAuth20ServiceImpl {
    private DefaultApi20 api;
    OAuthConfig config;
    String tokenType;

    public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
    }

    public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config, String tokenType) {
        super(api, config);
        this.api = api;
        this.config = config;
        this.tokenType = tokenType;
    }

    @Override
    public Token getAccessToken(Token requestToken, Verifier verifier) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        if (api.getAccessTokenVerb().equals(Verb.POST) || api.getAccessTokenVerb().equals(Verb.PUT)) {
            if (api instanceof com.ickstream.protocol.oauth.DefaultApi20) {
                request.addBodyParameter(((com.ickstream.protocol.oauth.DefaultApi20) api).getClientIdParameterName(), config.getApiKey());
                request.addBodyParameter(((com.ickstream.protocol.oauth.DefaultApi20) api).getClientSecretParameterName(), config.getApiSecret());
            } else {
                request.addBodyParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                request.addBodyParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
            }
            request.addBodyParameter(OAuthConstants.CODE, verifier.getValue());
            request.addBodyParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
            if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
            request.addBodyParameter("grant_type", "authorization_code");
        } else {
            if (api instanceof com.ickstream.protocol.oauth.DefaultApi20) {
                request.addQuerystringParameter(((com.ickstream.protocol.oauth.DefaultApi20) api).getClientIdParameterName(), config.getApiKey());
                request.addQuerystringParameter(((com.ickstream.protocol.oauth.DefaultApi20) api).getClientSecretParameterName(), config.getApiSecret());
            } else {
                request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
                request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
            }
            request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
            request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
            if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
            request.addQuerystringParameter("grant_type", "authorization_code");
        }
        Response response = request.send();
        return api.getAccessTokenExtractor().extract(response.getBody());
    }

    public Token refreshAccessToken(Token refreshToken) {
        OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
        try {
            request.addHeader("Authorization", "Basic " + Base64.encodeBase64String((config.getApiKey() + ":" + config.getApiSecret()).getBytes("UTF-8")));
            if (api.getAccessTokenVerb().equals(Verb.POST) || api.getAccessTokenVerb().equals(Verb.PUT)) {
                request.addBodyParameter("refresh_token", refreshToken.getToken());
                if (config.hasScope()) request.addBodyParameter(OAuthConstants.SCOPE, config.getScope());
                request.addBodyParameter("grant_type", "refresh_token");
            } else {
                request.addQuerystringParameter("refresh_token", refreshToken.getToken());
                if (config.hasScope()) request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
                request.addQuerystringParameter("grant_type", "refresh_token");
            }
            Response response = request.send();
            if (response.isSuccessful()) {
                return api.getAccessTokenExtractor().extract(response.getBody());
            } else if (response.getCode() == HttpStatus.SC_UNAUTHORIZED) {
                throw new UnauthorizedAccessException();
            } else {
                throw new RuntimeException("Failed to refresh token");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {
        if (api instanceof com.ickstream.protocol.oauth.DefaultApi20 &&
                ((com.ickstream.protocol.oauth.DefaultApi20) api).getTokenParameterName() != null) {
            request.addQuerystringParameter(((com.ickstream.protocol.oauth.DefaultApi20) api).getTokenParameterName(), accessToken.getToken());
        }
        if (tokenType != null) {
            request.addHeader("Authorization", tokenType + " " + accessToken.getToken());
        } else {
            request.addHeader("Authorization", "OAuth " + accessToken.getToken());
        }
    }
}
