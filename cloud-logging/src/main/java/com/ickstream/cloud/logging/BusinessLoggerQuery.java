/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.cloud.logging;

import java.util.Date;
import java.util.List;

public interface BusinessLoggerQuery {
    List<BusinessLoggerEntry> find(String userId, String excludeUserId, Boolean excludeAnonymousAccess, String deviceId, String serviceId, String servicePrefix, String servicePostfix, String excludeServicePrefix, String excludeServicePostfix, String method, Long minimumDuration, Date beforeTimestamp, Boolean onlyErrors, Integer count);
}
