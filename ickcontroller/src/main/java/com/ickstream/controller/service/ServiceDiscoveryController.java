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
