/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ServiceUrlResolver implements ServiceListener {
    private final Map<String, ServiceController> services = new HashMap<String, ServiceController>();

    public void onServiceAdded(ServiceController service) {
        synchronized (services) {
            services.put(service.getId(), service);
        }
    }

    public void onServiceRemoved(ServiceController service) {
        synchronized (services) {
            services.get(service.getId());
        }
    }

    public String resolveServiceUrl(String url) {
        String result = null;
        try {
            URI uri = new URI(url);
            if (uri.getScheme() != null && uri.getScheme().equals("service")) {
                String serviceId = uri.getAuthority();
                synchronized (services) {
                    if (services.containsKey(serviceId)) {
                        ServiceController serviceController = services.get(serviceId);
                        if (serviceController.getServiceUrl() != null) {
                            result = services.get(serviceId).getServiceUrl() + uri.getPath() + (uri.getQuery() != null ? "?" + uri.getQuery() : "") + (uri.getFragment() != null ? "#" + uri.getFragment() : "");
                        }
                    }
                }
            } else {
                result = url;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }
}
