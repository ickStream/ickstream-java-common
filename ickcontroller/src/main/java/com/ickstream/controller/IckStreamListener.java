/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller;

public interface IckStreamListener {
    void onCloudUnregistered();

    void onCloudRegistered(String accessToken);

    void onUnauthorized();

    void onError(int code, String message);

    void onNetworkInitialized();
}
