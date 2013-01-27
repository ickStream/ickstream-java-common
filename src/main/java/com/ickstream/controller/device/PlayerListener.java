/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.device;

public interface PlayerListener {
    void onPlayerAdded(PlayerDeviceController controller);

    void onPlayerRemoved(PlayerDeviceController controller);
}
