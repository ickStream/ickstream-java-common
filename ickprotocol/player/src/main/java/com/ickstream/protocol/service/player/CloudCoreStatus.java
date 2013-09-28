/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public enum CloudCoreStatus {
    /**
     * Player is registered in Cloud Core service and have a valid access token
     */
    REGISTERED,
    /**
     * Player is not yet registered in Cloud Core service and does not have a valid access token
     */
    UNREGISTERED
}
