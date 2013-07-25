/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import java.util.Map;

/**
 * Interface representing a business call.
 * Objects of this interface is passed to {@link BusinessLogger} to achieve logging in a service
 */
public interface BusinessCall {
    /**
     * Add a parameter
     *
     * @param name  The parameter name
     * @param value The parameter value
     */
    public void addParameter(String name, Object value);

    /**
     * Add multiple paramterss
     *
     * @param parameterMap A map with all parameters to be added
     */
    public void addParameters(Map<String, String> parameterMap);

    /**
     * Refresh the start timestamp of the call with current time
     */
    public void refreshTimestamp();

    /**
     * Update the duration of the call with current time
     */
    public void refreshDuration();
}
