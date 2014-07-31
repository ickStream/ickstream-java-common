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
