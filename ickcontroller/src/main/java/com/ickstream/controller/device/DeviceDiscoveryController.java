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

package com.ickstream.controller.device;

import com.ickstream.common.ickp2p.DiscoveryAdapter;
import com.ickstream.common.ickp2p.DiscoveryEvent;
import com.ickstream.common.ickp2p.ServiceType;
import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.controller.ObjectChangeListener;
import com.ickstream.controller.ThreadFramework;
import com.ickstream.protocol.service.core.CoreService;
import com.ickstream.protocol.service.core.DeviceResponse;
import com.ickstream.protocol.service.core.FindDevicesResponse;

import java.util.*;

public class DeviceDiscoveryController extends DiscoveryAdapter {
    protected final Map<String, Device> devices = new HashMap<String, Device>();
    private final List<ObjectChangeListener<Device>> deviceListeners = new ArrayList<ObjectChangeListener<Device>>();
    private MessageLogger messageLogger;
    private CoreService coreService;
    private ThreadFramework threadFramework;

    private enum EventSource {
        CLOUD,
        NETWORK
    }

    public DeviceDiscoveryController(ThreadFramework threadFramework, CoreService coreService, MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
        this.coreService = coreService;
        this.threadFramework = threadFramework;
    }

    public void setAccessToken(String accessToken) {
        coreService.setAccessToken(accessToken);
    }

    public void setCoreService(CoreService coreService) {
        if (this.coreService != coreService) {
            this.coreService = coreService;
            synchronized (devices) {
                Set<String> deviceIds = new HashSet<String>(devices.keySet());
                for (String deviceId : deviceIds) {
                    removeDiscoveredDevice(deviceId, EventSource.CLOUD);
                }
            }
            if (coreService != null) {
                refreshDevices();
            }
        }
    }

    public void refreshDevices() {
        coreService.findDevices(null, new MessageHandlerAdapter<FindDevicesResponse>() {
            @Override
            public void onMessage(FindDevicesResponse message) {
                Set<String> previous;
                synchronized (devices) {
                    previous = new HashSet<String>(devices.keySet());
                    for (DeviceResponse response : message.getItems()) {
                        previous.remove(response.getId());
                    }
                }
                for (DeviceResponse response : message.getItems()) {
                    addUpdateDiscoveredDevices(response.getId(), response.getName(), EventSource.CLOUD, response.getModel(), response.getAddress(), response.getPublicAddress());
                }
                for (String serviceId : previous) {
                    removeDiscoveredDevice(serviceId, EventSource.CLOUD);
                }
            }
        }, 5000);
    }

    @Override
    public void onConnectedDevice(final DiscoveryEvent event) {
        threadFramework.invoke(new Runnable() {
            @Override
            public void run() {
                if (messageLogger != null) {
                    messageLogger.onIncomingMessage(event.getDeviceId(), "{\"method\":\"CONNECTED\"}");
                }
                if (event.getServices().isType(ServiceType.PLAYER)) {
                    addUpdateDiscoveredDevices(event.getDeviceId(), event.getDeviceName(), EventSource.NETWORK);
                }
            }
        });
    }

    @Override
    public void onDisconnectedDevice(final String deviceId) {
        if (messageLogger != null) {
            threadFramework.invoke(new Runnable() {
                @Override
                public void run() {
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(deviceId, "{\"method\":\"DISCONNECTED\"}");
                    }
                    removeDiscoveredDevice(deviceId, EventSource.NETWORK);
                }
            });
        }
    }

    @Override
    public void onInitializedDevice(final DiscoveryEvent event) {
        if (messageLogger != null) {
            threadFramework.invoke(new Runnable() {
                @Override
                public void run() {
                    messageLogger.onIncomingMessage(event.getDeviceId(), "{\"method\":\"INITIALIZED\"}");
                }
            });
        }
    }

    @Override
    public void onDiscoveredDevice(final DiscoveryEvent event) {
        if (messageLogger != null) {
            threadFramework.invoke(new Runnable() {
                @Override
                public void run() {
                    messageLogger.onIncomingMessage(event.getDeviceId(), "{\"method\":\"DISCOVERED\"}");
                }
            });
        }
    }

    @Override
    public void onByeByeDevice(final String deviceId) {
        if (messageLogger != null) {
            threadFramework.invoke(new Runnable() {
                @Override
                public void run() {
                    messageLogger.onIncomingMessage(deviceId, "{\"method\":\"BYEBYE\"}");
                }
            });
        }
    }

    @Override
    public void onExpiredDevice(final String deviceId) {
        if (messageLogger != null) {
            threadFramework.invoke(new Runnable() {
                @Override
                public void run() {
                    messageLogger.onIncomingMessage(deviceId, "{\"method\":\"EXPIRED\"}");
                }
            });
        }
    }

    @Override
    public void onTerminatedDevice(final String deviceId) {
        if (messageLogger != null) {
            threadFramework.invoke(new Runnable() {
                @Override
                public void run() {
                    messageLogger.onIncomingMessage(deviceId, "{\"method\":\"TERMINATED\"]");
                }
            });
        }
    }

    public void addDeviceListener(ObjectChangeListener<Device> deviceListener) {
        synchronized (deviceListeners) {
            deviceListeners.add(deviceListener);
        }
    }

    public void removeDeviceListener(ObjectChangeListener<Device> deviceListener) {
        synchronized (deviceListeners) {
            deviceListeners.remove(deviceListener);
        }
    }

    protected void removeDiscoveredDevice(final String deviceId, EventSource eventSource) {
        Device device;
        boolean removedDevice = false;
        synchronized (devices) {
            device = devices.get(deviceId);
            if (device != null) {
                if ((device.getCloudState() != Device.CloudState.REGISTERED && eventSource == EventSource.NETWORK) ||
                        (device.getConnectionState() == Device.ConnectionState.DISCONNECTED && eventSource == EventSource.CLOUD)) {
                    devices.remove(deviceId);
                    removedDevice = true;
                }
            }
        }
        if (device != null && removedDevice) {
            for (ObjectChangeListener<Device> deviceListener : deviceListeners) {
                deviceListener.onRemoved(device);
            }
        } else if (device != null && device.getCloudState() == Device.CloudState.REGISTERED && eventSource == EventSource.NETWORK) {
            device.setConnectionState(Device.ConnectionState.DISCONNECTED);
        } else if (device != null && eventSource == EventSource.CLOUD) {
            device.setCloudState(Device.CloudState.UNREGISTERED);
        }
    }

    protected void addUpdateDiscoveredDevices(final String deviceId, final String deviceName, EventSource eventSource) {
        addUpdateDiscoveredDevices(deviceId, deviceName, eventSource, null, null, null);
    }

    protected void addUpdateDiscoveredDevices(final String deviceId, final String name, EventSource eventSource, final String model, final String address, final String publicAddress) {
        Device device;
        boolean newDevice = true;
        synchronized (devices) {
            if (devices.containsKey(deviceId)) {
                device = devices.get(deviceId);
                if (name != null && (eventSource == EventSource.CLOUD || device.getCloudState() != Device.CloudState.REGISTERED)) {
                    device.setName(name);
                }
                if (model != null) {
                    device.setModel(model);
                }
                if (address != null) {
                    device.setAddress(address);
                }
                newDevice = false;
            } else {
                device = new Device(deviceId);
                device.setName(name);
                device.setModel(model);
                device.setAddress(address);
                device.setPublicAddress(publicAddress);
                devices.put(deviceId, device);
            }
        }
        for (ObjectChangeListener<Device> deviceListener : deviceListeners) {
            if (newDevice) {
                deviceListener.onAdded(device);
            }
        }
        if (eventSource == EventSource.CLOUD) {
            device.setCloudState(Device.CloudState.REGISTERED);
        } else {
            device.setConnectionState(Device.ConnectionState.CONNECTED);
        }
    }

    public List<Device> getDevices() {
        synchronized (devices) {
            return new ArrayList<Device>(devices.values());
        }
    }
}
