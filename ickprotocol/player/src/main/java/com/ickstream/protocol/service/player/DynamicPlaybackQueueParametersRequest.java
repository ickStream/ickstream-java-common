/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.List;

public class DynamicPlaybackQueueParametersRequest {
    private List<DynamicPlaybackQueueServiceParameters> services;
    private Integer numberOfLeadingItems;
    private Integer maximumQueueLength;
    private DynamicPlaybackQueueSelectionParameters selectionParameters;

    public List<DynamicPlaybackQueueServiceParameters> getServices() {
        return services;
    }

    public void setServices(List<DynamicPlaybackQueueServiceParameters> services) {
        this.services = services;
    }

    public Integer getNumberOfLeadingItems() {
        return numberOfLeadingItems;
    }

    public void setNumberOfLeadingItems(Integer numberOfLeadingItems) {
        this.numberOfLeadingItems = numberOfLeadingItems;
    }

    public Integer getMaximumQueueLength() {
        return maximumQueueLength;
    }

    public void setMaximumQueueLength(Integer maximumQueueLength) {
        this.maximumQueueLength = maximumQueueLength;
    }

    public DynamicPlaybackQueueSelectionParameters getSelectionParameters() {
        return selectionParameters;
    }

    public void setSelectionParameters(DynamicPlaybackQueueSelectionParameters selectionParameters) {
        this.selectionParameters = selectionParameters;
    }
}

