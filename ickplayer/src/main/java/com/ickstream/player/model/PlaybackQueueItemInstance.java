/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.player.model;

import com.ickstream.protocol.common.data.StreamingReference;
import com.ickstream.protocol.service.player.PlaybackQueueItem;

import java.util.List;

public class PlaybackQueueItemInstance extends PlaybackQueueItem {
    private static int instanceCounter = 1;
    private int instanceId;

    public PlaybackQueueItemInstance() {
        instanceId = instanceCounter++;
    }

    public PlaybackQueueItemInstance(String id, String text, String type, String image) {
        super(id, text, type, image);
        instanceId = instanceCounter++;
    }

    public PlaybackQueueItemInstance(String id, String text, String type, String image, List<StreamingReference> streamingRefs) {
        super(id, text, type, image, streamingRefs);
        instanceId = instanceCounter++;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }
}
