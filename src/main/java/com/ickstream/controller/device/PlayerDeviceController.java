/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
 */

package com.ickstream.controller.device;

import com.ickstream.common.ickdiscovery.MessageSender;
import com.ickstream.common.jsonrpc.*;
import com.ickstream.controller.ThreadFramework;
import com.ickstream.protocol.service.core.*;
import com.ickstream.protocol.service.player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class PlayerDeviceController implements Observer, JsonRpcResponseHandler, JsonRpcRequestHandler {
    private static final String API_KEY = "987C3A70-A076-4312-8EF9-53E954B65F8B";
    Device device;
    List<MessageHandler<PlaylistResponse>> playlistListeners = new ArrayList<MessageHandler<PlaylistResponse>>();
    List<MessageHandler<PlayerStatusResponse>> playerStatusListeners = new ArrayList<MessageHandler<PlayerStatusResponse>>();
    List<PlayerStateListener> playerStateListeners = new ArrayList<PlayerStateListener>();
    MessageHandler<PlaylistChangedNotification> playlistChangedListener;
    MessageHandler<PlayerStatusResponse> playerStatusChangedListener;
    PlayerService playerService;
    CoreService coreService;
    ThreadFramework threadFramework;

    public PlayerDeviceController(MessageSender messageSender, Device device, MessageLogger messageLogger) {
        this((ThreadFramework) null, messageSender, device, messageLogger);
    }

    public PlayerDeviceController(ThreadFramework threadFramework, MessageSender messageSender, Device device, MessageLogger messageLogger) {
        this.threadFramework = threadFramework;
        playerService = new PlayerService(messageSender, device.getId());
        playerService.setMessageLogger(messageLogger);
        this.device = device;
        device.addObserver(this);
    }

    public void setCoreService(CoreService coreService) {
        this.coreService = coreService;
    }

    public String getId() {
        return device.getId();
    }

    public Device getDevice() {
        return device;
    }

    public void setPlaylistName(String name) {
        playerService.setPlaylistName(new SetPlaylistNameRequest(null, name), new MessageHandlerAdapter<SetPlaylistNameResponse>() {
            //TODO: Do we need to handle something ?
        });
    }

    public void setMute(Boolean muted) {
        playerService.setVolume(new VolumeRequest(null, null, muted), new MessageHandlerAdapter<VolumeResponse>() {
            //TODO: Do we need to handle something ?
        });
    }

    public void setVolume(Double volume) {
        playerService.setVolume(new VolumeRequest(volume, null, null), new MessageHandlerAdapter<VolumeResponse>() {
            //TODO: Do we need to handle something ?
        });
    }

    public void unRegister() {
        if (coreService != null) {
            coreService.removeDevice(new DeviceRequest(device.getId()), new MessageHandlerAdapter<Boolean>() {
                @Override
                public void onMessage(Boolean message) {
                    if (message) {
                        device.setCloudState(Device.CloudState.UNREGISTERED);
                        if (device.getConnectionState() == Device.ConnectionState.INITIALIZED) {
                            playerService.setPlayerConfiguration(new PlayerConfigurationRequest(null, ""), new MessageHandlerAdapter<PlayerConfigurationResponse>() {
                                //TODO: Do we need to do something ?
                            });
                        }
                    }
                }

            });
        }
    }

    public void register() {
        if (coreService != null) {
            AddDeviceWithHardwareIdRequest request = new AddDeviceWithHardwareIdRequest(
                    device.getId(),
                    device.getModel() != null ? device.getModel() : "TestModel",
                    device.getName(),
                    device.getAddress(),
                    API_KEY,
                    device.getHardwareId());

            if (device.getHardwareId() != null) {
                coreService.addDeviceWithHardwareId(request, new MessageHandlerAdapter<AddDeviceResponse>() {
                    @Override
                    public void onMessage(AddDeviceResponse message) {
                        playerService.setPlayerConfiguration(new PlayerConfigurationRequest(message.getName(), message.getAccessToken()), new MessageHandlerAdapter<PlayerConfigurationResponse>() {
                            @Override
                            public void onMessage(PlayerConfigurationResponse message) {
                                device.setCloudState(Device.CloudState.REGISTERED);
                            }
                        });
                    }
                });
            } else {
                coreService.addDevice(request, new MessageHandlerAdapter<AddDeviceResponse>() {
                    @Override
                    public void onMessage(AddDeviceResponse message) {
                        playerService.setPlayerConfiguration(new PlayerConfigurationRequest(message.getName(), message.getAccessToken()), new MessageHandlerAdapter<PlayerConfigurationResponse>() {
                            @Override
                            public void onMessage(PlayerConfigurationResponse message) {
                                device.setCloudState(Device.CloudState.REGISTERED);
                            }
                        });
                    }
                });
            }
        }
    }

    public void setName(String name) {
        playerService.setPlayerConfiguration(new PlayerConfigurationRequest(name, null), new MessageHandlerAdapter<PlayerConfigurationResponse>() {
            @Override
            public void onMessage(PlayerConfigurationResponse message) {
                device.setName(message.getPlayerName());
                //TODO: Generate player configuration listener event ?
            }
        });
        if (coreService != null && device.getCloudState() == Device.CloudState.REGISTERED) {
            coreService.setDeviceName(new SetDeviceNameRequest(device.getId(), name), new MessageHandlerAdapter<DeviceResponse>() {
                @Override
                public void onMessage(DeviceResponse message) {
                    device.setName(message.getName());
                    //TODO: Generate player configuration listener event ?
                }
            });
        }
    }

    public void play() {
        playerService.play(true);
    }

    public void pause() {
        playerService.play(false);
    }

    public void setTrack(Integer playlistPos) {
        playerService.setTrack(playlistPos, null);
    }

    public void removeTracks(PlaylistRemoveTracksRequest request) {
        playerService.removeTracks(request, null);
    }

    public void addTracks(PlaylistAddTracksRequest request) {
        playerService.addTracks(request, null);
    }

    @Override
    public void update(Observable observable, Object changeType) {
        if (observable instanceof Device) {
            Device device = (Device) observable;
            if (changeType.equals(Device.ConnectionState.INITIALIZED)) {
                for (PlayerStateListener playerStateListener : playerStateListeners) {
                    playerStateListener.onAvailable(PlayerDeviceController.this);
                }
            } else if (changeType.equals(Device.ConnectionState.CONNECTED)) {
                start();
            } else if (changeType.equals(Device.CloudState.REGISTERED)) {
                for (PlayerStateListener playerStateListener : playerStateListeners) {
                    playerStateListener.onRegistered(PlayerDeviceController.this);
                }
            } else if (changeType.equals(Device.CloudState.UNREGISTERED)) {
                for (PlayerStateListener playerStateListener : playerStateListeners) {
                    playerStateListener.onUnregistered(PlayerDeviceController.this);
                }
            } else if (changeType.equals(Device.ConnectionState.DISCONNECTED)) {
                for (PlayerStateListener playerStateListener : playerStateListeners) {
                    playerStateListener.onDisconnected(PlayerDeviceController.this);
                }
            }
        }
    }

    public void start() {
        if (device.getConnectionState() == Device.ConnectionState.CONNECTED) {
            device.setConnectionState(Device.ConnectionState.INITIALIZING);
            playerService.getPlayerConfiguration(new MessageHandlerAdapter<PlayerConfigurationResponse>() {
                @Override
                public void onMessage(PlayerConfigurationResponse message) {
                    if (message.getHardwareId() != null) {
                        device.setHardwareId(message.getHardwareId());
                    }
                    if (message.getPlayerModel() != null) {
                        device.setModel(message.getPlayerModel());
                    }
                    device.setConnectionState(Device.ConnectionState.INITIALIZED);
                }
            });
        }
    }

    public void refreshPlaylist() {
        playerService.getPlaylist(null, new MessageHandlerAdapter<PlaylistResponse>() {
            @Override
            public void onMessage(final PlaylistResponse message) {
                for (MessageHandler<PlaylistResponse> playlistListener : playlistListeners) {
                    playlistListener.onMessage(message);
                }
            }
        });
    }

    public void refreshPlayerStatus() {
        playerService.getPlayerStatus(new MessageHandlerAdapter<PlayerStatusResponse>() {
            @Override
            public void onMessage(final PlayerStatusResponse message) {
                for (MessageHandler<PlayerStatusResponse> playerStatusListener : playerStatusListeners) {
                    playerStatusListener.onMessage(message);
                }
            }
        });
    }

    public void addPlayerStateListener(PlayerStateListener listener) {
        playerStateListeners.add(listener);
    }

    public void removePlayerStateListener(PlayerStateListener listener) {
        playerStateListeners.remove(listener);
    }

    public void addPlaylistListener(MessageHandler<PlaylistResponse> playlistListener) {
        playlistListeners.add(playlistListener);
        if (playlistChangedListener == null) {
            playlistChangedListener = new MessageHandlerAdapter<PlaylistChangedNotification>() {
                @Override
                public void onMessage(PlaylistChangedNotification message) {
                    threadFramework.invoke(new Runnable() {
                        @Override
                        public void run() {
                            refreshPlaylist();
                        }
                    });
                }
            };
            playerService.addPlaylistChangedListener(playlistChangedListener);
        }
    }

    public void removePlaylistListener(MessageHandler<PlaylistResponse> playlistListener) {
        playlistListeners.remove(playlistListener);
        if (playlistListeners.size() == 0 && playlistChangedListener != null) {
            playerService.removePlaylistChangedListener(playlistChangedListener);
            playlistChangedListener = null;
        }
    }

    public void addPlayerStatusListener(MessageHandler<PlayerStatusResponse> playerStatusListener) {
        playerStatusListeners.add(playerStatusListener);
        if (playerStatusChangedListener == null) {
            playerStatusChangedListener = new MessageHandlerAdapter<PlayerStatusResponse>() {
                @Override
                public void onMessage(final PlayerStatusResponse message) {
                    threadFramework.invoke(new Runnable() {
                        @Override
                        public void run() {
                            for (MessageHandler<PlayerStatusResponse> playerStatusListener : playerStatusListeners) {
                                playerStatusListener.onMessage(message);
                            }
                        }
                    });
                }
            };
            playerService.addPlayerStatusChangedListener(playerStatusChangedListener);
        }
    }

    public void removePlayerStatusListener(MessageHandler<PlayerStatusResponse> playerStatusListener) {
        playerStatusListeners.remove(playerStatusListener);
        if (playerStatusListeners.size() == 0 && playerStatusChangedListener != null) {
            playerService.removePlayerStatusChangedListener(playerStatusChangedListener);
            playerStatusChangedListener = null;
        }
    }

    @Override
    public boolean onResponse(JsonRpcResponse response) {
        return playerService.onResponse(response);
    }

    @Override
    public boolean onRequest(JsonRpcRequest request) {
        return playerService.onRequest(request);
    }
}
