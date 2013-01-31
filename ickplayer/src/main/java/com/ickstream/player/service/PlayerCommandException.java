/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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
