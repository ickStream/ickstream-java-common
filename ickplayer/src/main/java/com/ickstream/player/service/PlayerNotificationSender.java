/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.service;

import com.ickstream.common.jsonrpc.JsonHelper;
import com.ickstream.common.jsonrpc.JsonRpcRequest;
import com.ickstream.common.jsonrpc.MessageSender;
import com.ickstream.protocol.service.player.PlayerStatusResponse;
import com.ickstream.protocol.service.player.PlaylistChangedNotification;

public class PlayerNotificationSender {
    private MessageSender messageSender;
    private JsonHelper jsonHelper = new JsonHelper();

    public PlayerNotificationSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void playerStatusChanged(PlayerStatusResponse status) {
        JsonRpcRequest notification = new JsonRpcRequest();
        notification.setMethod("playerStatusChanged");
        notification.setParams(jsonHelper.objectToJson(status));
        messageSender.sendMessage(jsonHelper.objectToString(notification));
    }

    public void playlistChanged(PlaylistChangedNotification playlistChanged) {
        JsonRpcRequest notification = new JsonRpcRequest();
        notification.setMethod("playlistChanged");
        notification.setParams(jsonHelper.objectToJson(playlistChanged));
        messageSender.sendMessage(jsonHelper.objectToString(notification));
    }
}
