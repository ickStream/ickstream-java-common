/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.oauth;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;

public class OAuth20ServiceImpl extends org.scribe.oauth.OAuth20ServiceImpl {
    private DefaultApi20 api;
    OAuthConfig config;

    public OAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
        super(api, config);
        this.api = api;
        this.config = config;
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

    @Override
    public void signRequest(Token accessToken, OAuthRequest request) {
        if (api instanceof com.ickstream.protocol.oauth.DefaultApi20 &&
                ((com.ickstream.protocol.oauth.DefaultApi20) api).getTokenParameterName() != null) {
            request.addQuerystringParameter(((com.ickstream.protocol.oauth.DefaultApi20) api).getTokenParameterName(), accessToken.getToken());
        }
        request.addHeader("Authorization", "OAuth " + accessToken.getToken());
    }
}
