/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.device;

public interface PlayerStateListener {
    void onAvailable(PlayerDeviceController controller);

    void onRegistered(PlayerDeviceController controller);

    void onUnregistered(PlayerDeviceController controller);

    void onDisconnected(PlayerDeviceController controller);
}
