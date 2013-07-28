/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public enum PlaybackQueueOrder {
    /**
     * Ordered according to the current playback queue
     */
    CURRENT,
    /**
     * Ordered according to the originally ordered playback queue
     */
    ORIGINAL
}
