/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.protocol.service.player;

import java.util.List;

public class DynamicPlaybackQueueServiceParameters {
    private String service;
    private Float weight;
    private List<DynamicPlaybackQueueSelectionParameters> selectionParameters;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public List<DynamicPlaybackQueueSelectionParameters> getSelectionParameters() {
        return selectionParameters;
    }

    public void setSelectionParameters(List<DynamicPlaybackQueueSelectionParameters> selectionParameters) {
        this.selectionParameters = selectionParameters;
    }
}
