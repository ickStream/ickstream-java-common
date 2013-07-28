/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import com.ickstream.protocol.common.ChunkedRequest;

public class PlaybackQueueRequest extends ChunkedRequest {
    private PlaybackQueueOrder order;

    public PlaybackQueueRequest() {
    }

    public PlaybackQueueRequest(Integer offset, Integer count) {
        super(offset, count);
    }

    public PlaybackQueueRequest(Integer offset, Integer count, PlaybackQueueOrder originalOrder) {
        super(offset, count);
        this.order = order;
    }

    public PlaybackQueueOrder getOrder() {
        return order;
    }

    public void setOrder(PlaybackQueueOrder order) {
        this.order = order;
    }
}
