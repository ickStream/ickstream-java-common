/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import com.ickstream.common.ickp2p.DiscoveryAdapter;
import com.ickstream.common.ickp2p.DiscoveryEvent;
import com.ickstream.common.ickp2p.ServiceType;
import com.ickstream.common.jsonrpc.MessageHandlerAdapter;
import com.ickstream.common.jsonrpc.MessageLogger;
import com.ickstream.controller.ObjectChangeListener;
import com.ickstream.controller.ThreadFramework;
import com.ickstream.protocol.service.core.CoreService;
import com.ickstream.protocol.service.core.FindServicesRequest;
import com.ickstream.protocol.service.core.FindServicesResponse;
import com.ickstream.protocol.service.core.ServiceResponse;

import java.util.*;

public class ServiceDiscoveryController extends DiscoveryAdapter {
    protected final Map<String, Service> cloudServices = new HashMap<String, Service>();
    protected final Map<String, Service> networkServices = new HashMap<String, Service>();
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

    public void setCoreService(CoreService coreService) {
        if (this.coreService != coreService) {
            this.coreService = coreService;
            synchronized (cloudServices) {
                Set<String> serviceIds = new HashSet<String>(cloudServices.keySet());
                for (String serviceId : serviceIds) {
                    removeDiscoveredServices(serviceId, EventSource.CLOUD);
                }
            }
            if (coreService != null) {
                refreshServices();
            }
        }
    }

    public void refreshServices() {
        coreService.findServices(new FindServicesRequest("content"), new MessageHandlerAdapter<FindServicesResponse>() {
            @Override
            public void onMessage(FindServicesResponse message) {
                Set<String> previous;
                synchronized (cloudServices) {
                    previous = new HashSet<String>(cloudServices.keySet());
                    for (ServiceResponse response : message.getItems()) {
                        previous.remove(response.getId());
                    }
                }
                for (ServiceResponse response : message.getItems()) {
                    addUpdateDiscoveredServices(response.getId(), response.getName(), EventSource.CLOUD, response.getUrl());
                }
                for (String serviceId : previous) {
                    removeDiscoveredServices(serviceId, EventSource.CLOUD);
                }
            }
        }, 5000);
    }

    @Override
    public void onConnectedDevice(final DiscoveryEvent event) {
        threadFramework.invoke(new Runnable() {
            @Override
            public void run() {
                if (event.getServices().isType(ServiceType.SERVICE)) {
                    addUpdateDiscoveredServices(event.getDeviceId(), event.getDeviceName(), EventSource.NETWORK);
                }
            }
        });
    }

    @Override
    public void onDisconnectedDevice(final String deviceId) {
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
        final Map<String, Service> services;
        if (eventSource == EventSource.NETWORK) {
            services = networkServices;
        } else {
            services = cloudServices;
        }
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
        if (eventSource == EventSource.NETWORK) {
            synchronized (networkServices) {
                service = networkServices.remove(serviceId);
            }
        } else {
            synchronized (cloudServices) {
                service = cloudServices.remove(serviceId);
            }
        }
        if (service != null) {
            for (ObjectChangeListener<Service> serviceListener : serviceListeners) {
                serviceListener.onRemoved(service);
            }
        }
    }
}
