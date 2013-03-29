/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

public enum RepeatMode {
    /** Playlist is played to the end and then playback stops */
    REPEAT_OFF,
    /** Playlist is played to the end and then restarted automatically from the beginning */
    REPEAT_PLAYLIST,
    /** Playlist is played to the end and then shuffled and automatically restarted from the beginning */
    REPEAT_PLAYLIST_AND_SHUFFLE;
}
