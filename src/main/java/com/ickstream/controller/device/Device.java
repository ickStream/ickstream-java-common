/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.device;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.Observable;

public class Device extends Observable {
    private String id;
    private String hardwareId;
    private String name;
    private String model;
    private String address;
    private String publicAddress;
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;
    private CloudState cloudState = CloudState.UNKNOWN;

    public enum ConnectionState {
        DISCONNECTED,
        CONNECTED,
        INITIALIZING,
        INITIALIZED,
    }

    public enum CloudState {
        UNKNOWN,
        REGISTERED,
        UNREGISTERED
    }

    public Device(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ConnectionState getConnectionState() {
        return connectionState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        if (!this.connectionState.equals(connectionState)) {
            this.connectionState = connectionState;
            setChanged();
            notifyObservers(connectionState);
        }
    }

    public CloudState getCloudState() {
        return cloudState;
    }

    public void setCloudState(CloudState cloudState) {
        if (!this.cloudState.equals(cloudState)) {
            this.cloudState = cloudState;
            setChanged();
            notifyObservers(cloudState);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getHardwareId() {
        return hardwareId;
    }

    public void setHardwareId(String hardwareId) {
        this.hardwareId = hardwareId;
    }

    @Override
    public String toString() {
        return name != null ? name : id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Device)) {
            return false;
        }
        return new EqualsBuilder().append(id, ((Device) o).getId()).isEquals();
    }
}
