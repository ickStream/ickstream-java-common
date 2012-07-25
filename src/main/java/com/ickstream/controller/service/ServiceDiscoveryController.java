/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.service;

import com.ickstream.common.ickdiscovery.DeviceListener;
import com.ickstream.common.ickdiscovery.ServiceType;
import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.controller.ObjectChangeListener;
import com.ickstream.controller.ThreadFramework;
import com.ickstream.protocol.service.core.CoreService;
import com.ickstream.protocol.service.core.FindServicesRequest;
import com.ickstream.protocol.service.core.FindServicesResponse;
import com.ickstream.protocol.service.core.ServiceResponse;

import java.util.*;

public class ServiceDiscoveryController implements DeviceListener {
    protected final Map<String, Service> services = new HashMap<String, Service>();
    protected List<ObjectChangeListener<Service>> serviceListeners = new ArrayList<ObjectChangeListener<Service>>();
    private ThreadFramework threadFramework;

    private enum EventSource {
        CLOUD,
        NETWORK
    }

    private CoreService coreService;
    private MessageLogger messageLogger;

    public ServiceDiscoveryController(ThreadFramework threadFramework, CoreService coreService, MessageLogger messageLogger) {
        this.coreService = coreService;
        this.messageLogger = messageLogger;
        this.threadFramework = threadFramework;
    }

    public void addServiceListener(ObjectChangeListener<Service> serviceListener) {
        serviceListeners.add(serviceListener);
    }

    public void removeServiceListener(ObjectChangeListener<Service> serviceListener) {
        serviceListeners.remove(serviceListener);
    }

    public void refreshServices() {
        coreService.findServices(new FindServicesRequest("content"), new MessageHandlerAdapter<FindServicesResponse>() {
            @Override
            public void onMessage(FindServicesResponse message) {
                Set<String> previous;
                synchronized (services) {
                    previous = new HashSet<String>(services.keySet());
                    for (ServiceResponse response : message.getItems_loop()) {
                        previous.remove(response.getId());
                    }
                }
                for (ServiceResponse response : message.getItems_loop()) {
                    addUpdateDiscoveredServices(response.getId(), response.getName(), EventSource.CLOUD, response.getUrl());
                }
                for (String serviceId : previous) {
                    removeDiscoveredServices(serviceId, EventSource.CLOUD);
                }
            }
        }, 5000);
    }

    @Override
    public void onDeviceAdded(final String deviceId, final String deviceName, final ServiceType type) {
        threadFramework.invoke(new Runnable() {
            @Override
            public void run() {
                if (type.isType(ServiceType.SERVICE)) {
                    addUpdateDiscoveredServices(deviceId, deviceName, EventSource.NETWORK);
                }
            }
        });
    }

    @Override
    public void onDeviceUpdated(final String deviceId, final String deviceName, final ServiceType type) {
        threadFramework.invoke(new Runnable() {
            @Override
            public void run() {
                if (type.isType(ServiceType.SERVICE)) {
                    addUpdateDiscoveredServices(deviceId, deviceName, EventSource.NETWORK);
                } else {
                    removeDiscoveredServices(deviceId, EventSource.NETWORK);
                }
            }
        });
    }

    @Override
    public void onDeviceRemoved(final String deviceId) {
        threadFramework.invoke(new Runnable() {
            @Override
            public void run() {
                removeDiscoveredServices(deviceId, EventSource.NETWORK);
            }
        });
    }

    protected void addUpdateDiscoveredServices(final String serviceId, String name, EventSource eventSource) {
        addUpdateDiscoveredServices(serviceId, name, eventSource, null);
    }

    protected void addUpdateDiscoveredServices(final String serviceId, final String name, EventSource eventSource, final String url) {
        Service service = new Service(serviceId, name, url);
        boolean newService = true;
        synchronized (services) {
            if (services.containsKey(serviceId)) {
                service = services.get(serviceId);
                if (name != null) {
                    service.setName(name);
                }
                if (url != null) {
                    service.setUrl(url);
                }
                newService = false;
            } else {
                services.put(serviceId, service);
            }
        }

        for (ObjectChangeListener<Service> serviceListener : serviceListeners) {
            if (newService) {
                serviceListener.onAdded(service);
            }
        }
    }

    protected void removeDiscoveredServices(final String serviceId, EventSource eventSource) {
        Service service;
        synchronized (services) {
            service = services.remove(serviceId);
        }
        if (service != null) {
            for (ObjectChangeListener<Service> serviceListener : serviceListeners) {
                serviceListener.onRemoved(service);
            }
        }
    }
}
