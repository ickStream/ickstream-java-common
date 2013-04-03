/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.ImageReference;
import com.ickstream.protocol.service.ServiceInformation;
import com.ickstream.protocol.service.backend.CloudService;
import com.ickstream.protocol.service.corebackend.CoreBackendService;
import com.ickstream.protocol.service.corebackend.ServiceResponse;

import java.util.List;

public abstract class AbstractCloudService implements CloudService {
    protected abstract String getServiceId();

    protected abstract CoreBackendService getCoreBackendService();

    @Override
    public ServiceInformation getServiceInformation() {
        ServiceResponse service = getCoreBackendService().getService();
        if (service != null) {
            ServiceInformation serviceInformation = new ServiceInformation();
            serviceInformation.setId(service.getId());
            serviceInformation.setName(service.getName());
            serviceInformation.setType(service.getType());
            serviceInformation.setUrl(service.getUrl());
            serviceInformation.setImages(getImages());
            return serviceInformation;
        }
        return null;
    }

    protected List<ImageReference> getImages() {
        return null;
    }
}
