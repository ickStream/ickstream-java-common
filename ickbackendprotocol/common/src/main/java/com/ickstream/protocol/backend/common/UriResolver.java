/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * URI resolver class which can be used by a service to resolve HTTP URI's to backend services.
 * All HTTP calls made by a service should be resolved using this object, the reason is that this makes it easy
 * to override the URL's in a unit test environment to achieve unit testing without being dependent on an online service
 * being available.
 */
public class UriResolver {
    /**
     * Resolve a URI, the default implementation provided by this class just returns the url passed as input
     *
     * @param url The URL which should be resolved to a URI
     * @return The resolved URI
     * @throws URISyntaxException
     */
    public URI resolve(String url) throws URISyntaxException {
        return new URI(url);
    }
}
