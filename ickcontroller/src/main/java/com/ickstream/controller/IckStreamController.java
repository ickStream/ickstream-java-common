/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.ickstream.common.ickp2p.IckP2p;
import com.ickstream.common.ickp2p.IckP2pException;
import com.ickstream.common.ickp2p.MessageListener;
import com.ickstream.common.ickp2p.ServiceType;
import com.ickstream.common.jsonrpc.*;
import com.ickstream.controller.device.Device;
import com.ickstream.controller.device.DeviceDiscoveryController;
import com.ickstream.controller.device.PlayerDeviceController;
import com.ickstream.controller.device.PlayerListener;
import com.ickstream.controller.service.*;
import com.ickstream.protocol.common.NetworkAddressHelper;
import com.ickstream.protocol.service.core.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class IckStreamController implements MessageListener {
    private String cloudCoreUrl;
    private String id;
    private String name;
    private String apiKey;
    private IckP2p ickP2p;
    private boolean ickP2pInitialized = false;
    private MessageLogger messageLogger;
    private DeviceDiscoveryController deviceDiscoveryController;
    private ServiceDiscoveryController serviceDiscoveryController;
    private Map<String, PlayerDeviceController> playerDeviceControllers = new HashMap<String, PlayerDeviceController>();
    private Map<String, LocalServiceController> localServiceControllers = new HashMap<String, LocalServiceController>();
    private Map<String, CloudServiceController> cloudServiceControllers = new HashMap<String, CloudServiceController>();
    private JsonHelper jsonHelper = new JsonHelper();
    private CoreService coreService;
    private List<IckStreamListener> ickStreamListeners = new ArrayList<IckStreamListener>();
    private List<PlayerListener> playerListeners = new ArrayList<PlayerListener>();
    private List<ServiceListener> serviceListeners = new ArrayList<ServiceListener>();

    private String accessToken;

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }


    private ObjectChangeListener<Device> deviceListener = new ObjectChangeListener<Device>() {
        @Override
        public void onRemoved(Device object) {
            PlayerDeviceController playerDeviceController = playerDeviceControllers.remove(object.getId());
            if (playerDeviceController != null) {
                for (PlayerListener playerListener : playerListeners) {
                    playerListener.onPlayerRemoved(playerDeviceController);
                }
            }
        }

        @Override
        public void onAdded(Device object) {
            PlayerDeviceController playerDeviceController = new PlayerDeviceController(apiKey, threadFramework, ickP2p, object, messageLogger);
            playerDeviceController.setCoreService(getCoreService(cloudCoreUrl));
            playerDeviceControllers.put(object.getId(), playerDeviceController);
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlayerAdded(playerDeviceController);
            }
            playerDeviceController.start();
        }
    };

    protected ServiceController removeLocalService(Service object) {
        return localServiceControllers.remove(object.getId());
    }

    protected ServiceController removeCloudService(Service object) {
        return cloudServiceControllers.remove(object.getId());
    }

    protected LocalServiceController addLocalService(Service object, com.ickstream.common.ickp2p.MessageSender messageSender, MessageLogger messageLogger) {
        return new LocalServiceController(object, messageSender, messageLogger);
    }

    protected CloudServiceController addCloudService(Service object, String accessToken, MessageLogger messageLogger) {
        return new CloudServiceController(object, accessToken, messageLogger);
    }

    private ObjectChangeListener<Service> serviceListener = new ObjectChangeListener<Service>() {
        @Override
        public void onRemoved(Service object) {
            ServiceController removedService = removeLocalService(object);
            if (removedService == null) {
                removedService = removeCloudService(object);
            }
            if (removedService != null) {
                for (ServiceListener serviceListener : serviceListeners) {
                    serviceListener.onServiceRemoved(removedService);
                }
            }
        }

        @Override
        public void onAdded(Service object) {
            ServiceController controller = null;
            if (object.getUrl() == null) {
                LocalServiceController localServiceController = addLocalService(object, ickP2p, messageLogger);
                if (localServiceController != null) {
                    localServiceControllers.put(object.getId(), localServiceController);
                    controller = localServiceController;
                }
            } else {
                CloudServiceController cloudServiceController = addCloudService(object, accessToken, messageLogger);
                if (cloudServiceController != null) {
                    cloudServiceControllers.put(object.getId(), cloudServiceController);
                    controller = cloudServiceController;
                }
            }
            if (controller != null) {
                for (ServiceListener serviceListener : serviceListeners) {
                    serviceListener.onServiceAdded(controller);
                }
            }
        }
    };
    private ThreadFramework threadFramework;

    public void addSystemListener(IckStreamListener ickStreamListener) {
        ickStreamListeners.add(ickStreamListener);
    }

    public void removeSystemListener(IckStreamListener ickStreamListener) {
        ickStreamListeners.remove(ickStreamListener);
    }

    public void unregisterCloud() {
        final CoreService coreService = getCoreService(cloudCoreUrl);
        if (coreService != null) {
            coreService.getDevice(new MessageHandlerAdapter<DeviceResponse>() {
                @Override
                public void onMessage(DeviceResponse message) {
                    coreService.removeDevice(new DeviceRequest(message.getId()), new MessageHandlerAdapter<Boolean>() {
                        @Override
                        public void onMessage(Boolean message) {
                            if (message) {
                                if (serviceDiscoveryController != null) {
                                    serviceDiscoveryController.setCoreService(null);
                                }
                                if (deviceDiscoveryController != null) {
                                    deviceDiscoveryController.setCoreService(null);
                                }
                                for (IckStreamListener ickStreamListener : ickStreamListeners) {
                                    ickStreamListener.onCloudUnregistered();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    public void registerCloud(String cloudCoreUrl, String id, String name, String userToken) {
        registerCloud(cloudCoreUrl, id, name, userToken, null);
    }

    public void registerCloud(final String cloudCoreUrl, String id, String name, String userToken, final String hardwareIdAppPostfix) {
        final CoreService coreService = getCoreService(cloudCoreUrl);
        if (coreService == null) {
            return;
        }
        coreService.setAccessToken(userToken);
        CreateDeviceRegistrationTokenRequest request = new CreateDeviceRegistrationTokenRequest();
        request.setId(id);
        request.setName(name);
        request.setApplicationId(apiKey);

        coreService.createDeviceRegistrationToken(request, new MessageHandlerAdapter<String>() {
            @Override
            public void onMessage(String deviceRegistrationToken) {
                AddDeviceRequest request = new AddDeviceRequest();
                String networkAddress = NetworkAddressHelper.getNetworkAddress();
                String hardwareId = NetworkAddressHelper.getNetworkHardwareAddress();
                if (hardwareId != null) {
                    hardwareId = UUID.nameUUIDFromBytes(hardwareId.getBytes()).toString();
                    if (hardwareIdAppPostfix != null) {
                        hardwareId += hardwareIdAppPostfix;
                    }
                }
                request.setAddress(networkAddress);
                request.setHardwareId(hardwareId);
                request.setApplicationId(apiKey);
                CoreServiceFactory.getCoreService(cloudCoreUrl, deviceRegistrationToken, messageLogger).addDevice(request, new MessageHandlerAdapter<AddDeviceResponse>() {
                    @Override
                    public void onMessage(AddDeviceResponse message) {
                        IckStreamController.this.id = message.getId();
                        String accessToken = message.getAccessToken();
                        for (PlayerDeviceController controller : playerDeviceControllers.values()) {
                            controller.setCoreService(coreService);
                        }
                        for (IckStreamListener ickStreamListener : ickStreamListeners) {
                            ickStreamListener.onCloudRegistered(accessToken);
                        }
                        if (serviceDiscoveryController != null) {
                            serviceDiscoveryController.setCoreService(coreService);
                        }
                        if (deviceDiscoveryController != null) {
                            deviceDiscoveryController.setCoreService(coreService);
                        }
                    }

                    @Override
                    public void onError(int code, String message, String data) {
                        for (IckStreamListener ickStreamListener : ickStreamListeners) {
                            if (code == JsonRpcError.UNAUTHORIZED) {
                                ickStreamListener.onUnauthorized();
                            } else {
                                ickStreamListener.onError(code, message + (data != null ? "\n" + data : ""));
                            }
                        }
                    }
                }, 20000);
            }

            @Override
            public void onError(int code, String message, String data) {
                for (IckStreamListener ickStreamListener : ickStreamListeners) {
                    if (code == JsonRpcError.UNAUTHORIZED) {
                        ickStreamListener.onUnauthorized();
                    } else {
                        ickStreamListener.onError(code, message + (data != null ? "\n" + data : ""));
                    }
                }
            }
        }, 20000);
    }

    public CoreService getCoreService(String cloudCoreUrl) {
        if (coreService == null || !this.cloudCoreUrl.equals(cloudCoreUrl)) {
            this.cloudCoreUrl = cloudCoreUrl;
            coreService = CoreServiceFactory.getCoreService(cloudCoreUrl, null, messageLogger);
            coreService.setAccessToken(accessToken);
        }
        return coreService;
    }

    public void setAccessToken(String cloudCoreUrl, String accessToken) {
        this.accessToken = accessToken;
        CoreService coreService = getCoreService(cloudCoreUrl);
        if (coreService != null) {
            coreService.setAccessToken(accessToken);
        }
        for (CloudServiceController cloudServiceController : cloudServiceControllers.values()) {
            cloudServiceController.setAccessToken(accessToken);
        }
    }

    protected void start(String deviceId, String deviceName) {
        if (!ickP2pInitialized) {
            deviceDiscoveryController = new DeviceDiscoveryController(threadFramework, getCoreService(cloudCoreUrl), messageLogger);
            deviceDiscoveryController.addDeviceListener(deviceListener);
            serviceDiscoveryController = new ServiceDiscoveryController(threadFramework, getCoreService(cloudCoreUrl), messageLogger);
            serviceDiscoveryController.addServiceListener(serviceListener);
            ickP2p.addMessageListener(IckStreamController.this);
            ickP2p.addDiscoveryListener(deviceDiscoveryController);
            ickP2p.addDiscoveryListener(serviceDiscoveryController);
            String ip = NetworkAddressHelper.getNetworkAddress();
            try {
                System.out.println("create(\"" + deviceName + "\",\"" + deviceId + "\",NULL,NULL,NULL," + ServiceType.CONTROLLER + ")");
                ickP2p.create(deviceName, deviceId, null, null, null, ServiceType.CONTROLLER);
                System.out.println("addInterface(\"" + ip + "\",NULL");
                ickP2p.addInterface(ip, null);
                ickP2p.resume();
                ickP2pInitialized = true;
                for (IckStreamListener ickStreamListener : ickStreamListeners) {
                    ickStreamListener.onNetworkInitialized();
                }
                refreshDevices();
                refreshServices();
            } catch (IckP2pException e) {
                ickP2p.removeDiscoveryListener(deviceDiscoveryController);
                ickP2p.removeDiscoveryListener(serviceDiscoveryController);
                ickP2p.removeMessageListener(IckStreamController.this);
                serviceDiscoveryController.removeServiceListener(serviceListener);
                deviceDiscoveryController.removeDeviceListener(deviceListener);
                serviceDiscoveryController = null;
                deviceDiscoveryController = null;
                for (IckStreamListener ickStreamListener : ickStreamListeners) {
                    ickStreamListener.onError(e.getErrorCode(), "ickP2p initialization error: " + e.getErrorCode());
                }
            }
        }
    }

    protected void refreshServices() {
        serviceDiscoveryController.refreshServices();
    }

    protected void refreshDevices() {
        deviceDiscoveryController.refreshDevices();
    }

    public void shutdown() {
        if (ickP2pInitialized) {
            try {
                ickP2p.end();
            } catch (IckP2pException e) {
                e.printStackTrace();
            }
            ickP2pInitialized = false;
        }
    }

    public void start() {
        CoreService coreService = getCoreService(cloudCoreUrl);
        if (coreService != null) {
            coreService.setDeviceAddress(new SetDeviceAddressRequest(NetworkAddressHelper.getNetworkAddress()), new MessageHandlerAdapter<DeviceResponse>() {
                @Override
                public void onMessage(DeviceResponse message) {
                    setId(message.getId());
                    setName(message.getName());
                    start(getId(), getName());
                }

                @Override
                public void onError(int code, String message, String data) {
                    for (IckStreamListener ickStreamListener : ickStreamListeners) {
                        if (code == JsonRpcError.UNAUTHORIZED) {
                            ickStreamListener.onUnauthorized();
                        } else {
                            ickStreamListener.onError(code, message + (data != null ? "\n" + data : ""));
                        }
                    }
                }
            }, 5000);
        } else if (getId() != null) {
            start(getId(), getName());
        }
    }

    public void stopNetwork() {
        deviceDiscoveryController.removeDeviceListener(deviceListener);
        serviceDiscoveryController.removeServiceListener(serviceListener);

        try {
            ickP2p.end();
        } catch (IckP2pException e) {
            e.printStackTrace();
        }
    }

    public IckStreamController(String apiKey, ThreadFramework threadFramework, IckP2p ickP2p, MessageLogger messageLogger) {
        this.apiKey = apiKey;
        this.messageLogger = messageLogger;
        this.threadFramework = threadFramework;
        this.ickP2p = ickP2p;
    }

    public IckStreamController(MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
    }

    public void addPlayerListener(PlayerListener listener) {
        playerListeners.add(listener);
    }

    public void removePlayerListener(PlayerListener listener) {
        playerListeners.remove(listener);
    }

    public void addContentServiceListener(ServiceListener listener) {
        serviceListeners.add(listener);
    }

    public void removeContentServiceListener(ServiceListener listener) {
        serviceListeners.remove(listener);
    }

    @Override
    public void onMessage(String sourceDeviceId, ServiceType sourceServiceType, String targetDeviceId, ServiceType targetServiceType, byte[] message) {
        LocalServiceController localServiceController = localServiceControllers.get(sourceDeviceId);
        if (localServiceController != null) {
            try {
                JsonNode jsonMessage = jsonHelper.stringToObject(new String(message, "UTF-8"), JsonNode.class);
                if (jsonMessage != null) {
                    String jsonMessageString = jsonHelper.objectToString(jsonMessage);
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(sourceDeviceId, jsonMessageString);
                    }
                    if (!jsonMessage.has("method")) {
                        localServiceController.onResponse(jsonHelper.jsonToObject(jsonMessage, JsonRpcResponse.class));
                    }
                } else {
                    System.err.println("Unable to parse incoming message from " + sourceDeviceId + "(" + sourceServiceType + "): " + new String(message, "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                // Just ignore, all platforms we support should support UTF-8
                e.printStackTrace();
            }
        }
        PlayerDeviceController playerDeviceController = playerDeviceControllers.get(sourceDeviceId);
        if (playerDeviceController != null) {
            try {
                JsonNode jsonMessage = jsonHelper.stringToObject(new String(message, "UTF-8"), JsonNode.class);
                if (jsonMessage != null) {
                    String jsonMessageString = jsonHelper.objectToString(jsonMessage);
                    if (messageLogger != null) {
                        messageLogger.onIncomingMessage(sourceDeviceId, jsonMessageString);
                    }
                    if (jsonMessage.has("method")) {
                        playerDeviceController.onRequest(jsonHelper.jsonToObject(jsonMessage, JsonRpcRequest.class));
                    } else {
                        playerDeviceController.onResponse(jsonHelper.jsonToObject(jsonMessage, JsonRpcResponse.class));
                    }
                } else {
                    System.err.println("Unable to parse incoming message from " + sourceDeviceId + "(" + sourceServiceType + "): " + new String(message, "UTF-8"));
                }
            } catch (UnsupportedEncodingException e) {
                // Just ignore, all platforms we support should support UTF-8
                e.printStackTrace();
            }
        }
    }
}
