package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.corebackend.DeviceResponse;
import com.ickstream.protocol.service.corebackend.UserResponse;

public class BackendRequestContext {
    private DeviceResponse device;
    private UserResponse user;

    public DeviceResponse getDevice() {
        return device;
    }

    public void setDevice(DeviceResponse device) {
        this.device = device;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
