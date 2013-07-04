/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import com.ickstream.protocol.common.ChunkedRequest;

public class PlaybackQueueRequest extends ChunkedRequest {
    private Boolean originalOrder;

    public PlaybackQueueRequest() {
    }

    public PlaybackQueueRequest(Integer offset, Integer count) {
        super(offset, count);
    }

    public PlaybackQueueRequest(Integer offset, Integer count, Boolean originalOrder) {
        super(offset, count);
        this.originalOrder = originalOrder;
    }

    public Boolean getOriginalOrder() {
        return originalOrder;
    }

    public void setOriginalOrder(Boolean originalOrder) {
        this.originalOrder = originalOrder;
    }
}
