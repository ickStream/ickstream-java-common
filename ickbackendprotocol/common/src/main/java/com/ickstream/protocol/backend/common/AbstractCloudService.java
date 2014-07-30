/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import com.ickstream.protocol.service.ImageReference;
import com.ickstream.protocol.service.ServiceInformation;
import com.ickstream.protocol.service.corebackend.CoreBackendService;
import com.ickstream.protocol.service.corebackend.ServiceResponse;

import java.util.List;

/**
 * Abstract class that mostly implements the {@link CloudService} interface and just of loads implementation of
 * specific parts which differs between different services to the sub class
 */
public abstract class AbstractCloudService implements CloudService {
    /**
     * Returns the {@link CoreBackendService} client implementation this service want to use.
     * Typically the sub class should use the {@link com.ickstream.protocol.service.corebackend.CoreBackendServiceFactory} class to implement this.
     *
     * @return The CoreBackendService client instance that should be used
     */
    protected abstract CoreBackendService getCoreBackendService();

    /**
     * Implements {@link com.ickstream.protocol.backend.common.CloudService#getServiceInformation()} method by using
     * information retrieved from {@link com.ickstream.protocol.service.corebackend.CoreBackendService#getService()}.
     * The methods also uses the {@link #getImages()} method to make it possible for the sub class to override which
     * images to show in the returned service information
     *
     * @return Information about this service
     */
    @Override
    public ServiceInformation getServiceInformation() {
        ServiceResponse service = getCoreBackendService().getService();
        if (service != null) {
            ServiceInformation serviceInformation = new ServiceInformation();
            serviceInformation.setId(service.getId());
            serviceInformation.setName(service.getName());
            serviceInformation.setType(service.getType());
            serviceInformation.getMainCategory();
            serviceInformation.setUrl(service.getUrl());
            serviceInformation.setImages(getImages());
            return serviceInformation;
        }
        return null;
    }

    /**
     * Empty implementation that doesn't return any images, should be overridden by sub classes which provides images
     * specific to the service
     *
     * @return A list of images that can be used to represent the service
     */
    protected List<ImageReference> getImages() {
        return null;
    }
}
