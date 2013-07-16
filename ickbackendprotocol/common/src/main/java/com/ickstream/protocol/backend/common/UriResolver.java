/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import java.net.URI;
import java.net.URISyntaxException;

public class UriResolver {
    public URI resolve(String url) throws URISyntaxException {
        return new URI(url);
    }
}
