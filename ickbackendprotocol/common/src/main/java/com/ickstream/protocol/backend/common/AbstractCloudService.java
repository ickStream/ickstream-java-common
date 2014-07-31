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
