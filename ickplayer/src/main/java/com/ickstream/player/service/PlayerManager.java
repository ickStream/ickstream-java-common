/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.service;

public interface PlayerManager {
    void setAccessToken(String accessToken);

    void setName(String playerName);

    String getName();

    String getModel();

    String getHardwareId();

    Boolean play();

    Boolean pause();

    Double getVolume();

    void setVolume(Double volume);

    Double getSeekPosition();
}
