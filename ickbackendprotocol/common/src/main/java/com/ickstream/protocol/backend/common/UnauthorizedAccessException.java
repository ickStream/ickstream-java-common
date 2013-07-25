/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

/**
 * This exception should be thrown by services when they are detecting unauthorized access.
 * <p/>
 * Please note that the service does not have to handle the initial authorization if the service are configured
 * with the {@link AbstractOAuthAuthorizationFilter}, this exception is mainly used for the cases when a service
 * do more detailed authorization inside the service implementation.
 */
public class UnauthorizedAccessException extends RuntimeException {
}
