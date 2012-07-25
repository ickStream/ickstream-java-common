/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.device;

public interface PlayerStateListener {
    void onAvailable(PlayerDeviceController controller);

    void onRegistered(PlayerDeviceController controller);

    void onUnregistered(PlayerDeviceController controller);

    void onDisconnected(PlayerDeviceController controller);
}
