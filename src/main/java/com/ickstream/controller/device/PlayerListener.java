/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.device;

public interface PlayerListener {
    void onPlayerAdded(PlayerDeviceController controller);

    void onPlayerRemoved(PlayerDeviceController controller);
}
