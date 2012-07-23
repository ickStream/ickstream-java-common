/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.device;

public interface DeviceListener {
    void onDeviceAdded(String deviceId, String deviceName, int services);
    void onDeviceUpdated(String deviceId, String deviceName, int services);
    void onDeviceRemoved(String deviceId);
}
