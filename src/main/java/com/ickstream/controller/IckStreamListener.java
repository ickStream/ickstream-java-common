/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller;

public interface IckStreamListener {
    void onCloudUnregistered();

    void onCloudRegistered(String accessToken);

    void onUnauthorized();

    void onError(int code, String message);

    void onNetworkInitialized();
}
