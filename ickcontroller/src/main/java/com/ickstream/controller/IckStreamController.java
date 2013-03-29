/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller;

import com.ickstream.common.ickdiscovery.IckDiscovery;
import com.ickstream.common.ickdiscovery.MessageListener;
import com.ickstream.common.ickdiscovery.ServiceType;
import com.ickstream.common.jsonrpc.*;
import com.ickstream.controller.device.Device;
import com.ickstream.controller.device.DeviceDiscoveryController;
import com.ickstream.controller.device.PlayerDeviceController;
import com.ickstream.controller.device.PlayerListener;
import com.ickstream.controller.service.*;
import com.ickstream.protocol.common.NetworkAddressHelper;
import com.ickstream.protocol.common.ServiceFactory;
import com.ickstream.protocol.service.core.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class IckStreamController implements MessageListener {
    private String id;
    private static final String API_KEY = "987C3A70-A076-4312-8EF9-53E954B65F8B";
    private IckDiscovery ickDiscovery;
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
            PlayerDeviceController playerDeviceController = new PlayerDeviceController(threadFramework, ickDiscovery, object, messageLogger);
            playerDeviceController.setCoreService(getCoreService());
            playerDeviceControllers.put(object.getId(), playerDeviceController);
            for (PlayerListener playerListener : playerListeners) {
                playerListener.onPlayerAdded(playerDeviceController);
            }
            playerDeviceController.start();
        }
    };

    private ObjectChangeListener<Service> serviceListener = new ObjectChangeListener<Service>() {
        @Override
        public void onRemoved(Service object) {
            ServiceController removedService = localServiceControllers.remove(object.getId());
            if (removedService == null) {
                removedService = cloudServiceControllers.remove(object.getId());
            }
            for (ServiceListener serviceListener : serviceListeners) {
                serviceListener.onServiceRemoved(removedService);
            }
        }

        @Override
        public void onAdded(Service object) {
            ServiceController controller;
            if (object.getUrl() == null) {
                LocalServiceController localServiceController = new LocalServiceController(object, ickDiscovery, messageLogger);
                localServiceControllers.put(object.getId(), localServiceController);
                controller = localServiceController;
            } else {
                CloudServiceController cloudServiceController = new CloudServiceController(object, accessToken, messageLogger);
                cloudServiceControllers.put(object.getId(), cloudServiceController);
                controller = cloudServiceController;
            }
            for (ServiceListener serviceListener : serviceListeners) {
                serviceListener.onServiceAdded(controller);
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
        getCoreService().getDevice(new MessageHandlerAdapter<DeviceResponse>() {
            @Override
            public void onMessage(DeviceResponse message) {
                getCoreService().removeDevice(new DeviceRequest(message.getId()), new MessageHandlerAdapter<Boolean>() {
                    @Override
                    public void onMessage(Boolean message) {
                        if (message) {
                            for (IckStreamListener ickStreamListener : ickStreamListeners) {
                                ickStreamListener.onCloudUnregistered();
                            }
                        }
                    }
                });
            }
        });
    }

    public void registerCloud(String model, String name, String userToken) {
        getCoreService().setAccessToken(userToken);
        AddDeviceWithHardwareIdRequest request = new AddDeviceWithHardwareIdRequest();
        request.setModel(model);
        request.setApplicationId(API_KEY);
        request.setName(name);
        request.setAddress(NetworkAddressHelper.getNetworkAddress());
        String hardwareId = NetworkAddressHelper.getNetworkHardwareAddress();
        if (hardwareId != null) {
            hardwareId = UUID.nameUUIDFromBytes(hardwareId.getBytes()).toString();
        }
        request.setHardwareId(hardwareId);
        if (hardwareId != null) {
            getCoreService().addDeviceWithHardwareId(request, new MessageHandlerAdapter<AddDeviceResponse>() {
                @Override
                public void onMessage(AddDeviceResponse message) {
                    id = message.getId();
                    String accessToken = message.getAccessToken();
                    for (IckStreamListener ickStreamListener : ickStreamListeners) {
                        ickStreamListener.onCloudRegistered(accessToken);
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
        } else {
            getCoreService().addDevice(request, new MessageHandlerAdapter<AddDeviceResponse>() {
                @Override
                public void onMessage(AddDeviceResponse message) {
                    id = message.getId();
                    String accessToken = message.getAccessToken();
                    for (IckStreamListener ickStreamListener : ickStreamListeners) {
                        ickStreamListener.onCloudRegistered(accessToken);
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
    }

    public CoreService getCoreService() {
        if (coreService == null) {
            coreService = ServiceFactory.getCoreService(null, messageLogger);
            coreService.setAccessToken(accessToken);
        }
        return coreService;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        getCoreService().setAccessToken(accessToken);
        for (CloudServiceController cloudServiceController : cloudServiceControllers.values()) {
            cloudServiceController.setAccessToken(accessToken);
        }
    }

    private void start(String deviceId, String deviceName) {
        deviceDiscoveryController = new DeviceDiscoveryController(threadFramework, getCoreService(), messageLogger);
        deviceDiscoveryController.addDeviceListener(deviceListener);
        serviceDiscoveryController = new ServiceDiscoveryController(threadFramework, getCoreService(), messageLogger);
        serviceDiscoveryController.addServiceListener(serviceListener);
        ickDiscovery.addMessageListener(IckStreamController.this);
        ickDiscovery.addDeviceListener(deviceDiscoveryController);
        ickDiscovery.addDeviceListener(serviceDiscoveryController);
        ickDiscovery.initDiscovery(deviceId, NetworkAddressHelper.getNetworkAddress(), deviceName, null);
        ickDiscovery.addService(ServiceType.CONTROLLER);
        for (IckStreamListener ickStreamListener : ickStreamListeners) {
            ickStreamListener.onNetworkInitialized();
        }
        serviceDiscoveryController.refreshServices();
        deviceDiscoveryController.refreshDevices();
    }

    public void shutdown() {
        if (ickDiscovery != null) {
            ickDiscovery.endDiscovery();
            ickDiscovery = null;
        }
    }

    public void start() {
        getCoreService().setDeviceAddress(new SetDeviceAddressRequest(NetworkAddressHelper.getNetworkAddress()), new MessageHandlerAdapter<DeviceResponse>() {
            @Override
            public void onMessage(DeviceResponse message) {
                id = message.getId();
                start(message.getId(), message.getName());
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
    }

    public void stopNetwork() {
        deviceDiscoveryController.removeDeviceListener(deviceListener);
        serviceDiscoveryController.removeServiceListener(serviceListener);

        ickDiscovery.endDiscovery();
    }

    public IckStreamController(ThreadFramework threadFramework, IckDiscovery ickDiscovery, MessageLogger messageLogger) {
        this.messageLogger = messageLogger;
        this.threadFramework = threadFramework;
        this.ickDiscovery = ickDiscovery;
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
    public void onMessage(String deviceId, byte[] message) {
        LocalServiceController localServiceController = localServiceControllers.get(deviceId);
        if (localServiceController != null) {
            try {
                JsonNode jsonMessage = jsonHelper.stringToObject(new String(message, "UTF-8"), JsonNode.class);
                String jsonMessageString = jsonHelper.objectToString(jsonMessage);
                if (messageLogger != null) {
                    messageLogger.onIncomingMessage(deviceId, jsonMessageString);
                }
                if (!jsonMessage.has("method")) {
                    localServiceController.onResponse(jsonHelper.jsonToObject(jsonMessage, JsonRpcResponse.class));
                }
            } catch (UnsupportedEncodingException e) {
                // Just ignore, all platforms we support should support UTF-8
                e.printStackTrace();
            }
        }
        PlayerDeviceController playerDeviceController = playerDeviceControllers.get(deviceId);
        if (playerDeviceController != null) {
            try {
                JsonNode jsonMessage = jsonHelper.stringToObject(new String(message, "UTF-8"), JsonNode.class);
                String jsonMessageString = jsonHelper.objectToString(jsonMessage);
                if (messageLogger != null) {
                    messageLogger.onIncomingMessage(deviceId, jsonMessageString);
                }
                if (jsonMessage.has("method")) {
                    playerDeviceController.onRequest(jsonHelper.jsonToObject(jsonMessage, JsonRpcRequest.class));
                } else {
                    playerDeviceController.onResponse(jsonHelper.jsonToObject(jsonMessage, JsonRpcResponse.class));
                }
            } catch (UnsupportedEncodingException e) {
                // Just ignore, all platforms we support should support UTF-8
                e.printStackTrace();
            }
        }
    }
}
