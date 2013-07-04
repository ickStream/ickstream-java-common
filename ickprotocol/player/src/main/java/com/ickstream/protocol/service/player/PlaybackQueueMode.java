/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public enum PlaybackQueueMode {
    /**
     * Playback queue is played to the end and then playback stops
     */
    QUEUE,
    /**
     * Playback queue is shuffled and then played to the end and then playback stops
     */
    QUEUE_SHUFFLE,
    /**
     * Playback queue is played to the end and then restarted automatically from the beginning
     */
    QUEUE_REPEAT,
    /**
     * Playback queue is played to the end and then shuffled and automatically restarted from the beginning
     */
    QUEUE_REPEAT_SHUFFLE,
    /**
     * Playback queue is automatically filled by player based on dynamic playback queue parameters
     */
    QUEUE_DYNAMIC
}
