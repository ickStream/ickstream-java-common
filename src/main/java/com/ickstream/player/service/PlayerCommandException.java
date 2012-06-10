/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.player.service;

public class PlayerCommandException extends RuntimeException {
    public PlayerCommandException(String detailMessage) {
        super(detailMessage);
    }

    public PlayerCommandException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
