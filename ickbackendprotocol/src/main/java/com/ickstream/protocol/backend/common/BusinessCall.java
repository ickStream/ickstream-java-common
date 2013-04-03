/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.backend.common;

import java.util.Map;

public interface BusinessCall {
    public void addParameter(String name, Object value);

    public void addParameters(Map<String, String> parameterMap);

    public void refreshTimestamp();

    public void refreshDuration();
}
