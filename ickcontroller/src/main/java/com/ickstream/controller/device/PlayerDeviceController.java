/*
 * Copyright (c) 2013-2014, ickStream GmbH
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *   * Neither the name of ickStream nor the names of its contributors
 *     may be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ickstream.controller.device;

import com.ickstream.common.ickp2p.MessageSender;
import com.ickstream.common.ickp2p.ServiceType;
import com.ickstream.common.jsonrpc.*;
import com.ickstream.controller.ThreadFramework;
import com.ickstream.protocol.service.core.*;
import com.ickstream.protocol.service.player.*;

import java.util.*;

public class PlayerDeviceController implements Observer, JsonRpcResponseHandler, JsonRpcRequestHandler {
    private String apiKey;
    Device device;
    List<MessageHandler<PlaybackQueueResponse>> playbackQueueListeners = new ArrayList<MessageHandler<PlaybackQueueResponse>>();
    List<MessageHandler<PlayerStatusResponse>> playerStatusListeners = new ArrayList<MessageHandler<PlayerStatusResponse>>();
    Set<PlayerStateListener> playerStateListeners = new HashSet<PlayerStateListener>();
    MessageHandler<PlaybackQueueChangedNotification> playbackQueueChangedListener;
    MessageHandler<PlayerStatusResponse> playerStatusChangedListener;
    PlayerService playerService;
    CoreService coreService;
    ThreadFramework threadFramework;

    public PlayerDeviceController(String apiKey, MessageSender messageSender, Device device, MessageLogger messageLogger) {
        this(apiKey, (ThreadFramework) null, messageSender, device, messageLogger);
    }

    public PlayerDeviceController(String apiKey, ThreadFramework threadFramework, MessageSender messageSender, Device device, MessageLogger messageLogger) {
        this.apiKey = apiKey;
        this.threadFramework = threadFramework;
        playerService = new PlayerService(ServiceType.CONTROLLER, messageSender, device.getId());
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
                            PlayerConfigurationRequest request = new PlayerConfigurationRequest();
                            request.setDeviceRegistrationToken("");
                            playerService.setPlayerConfiguration(request, new MessageHandlerAdapter<PlayerConfigurationResponse>() {
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
            CreateDeviceRegistrationTokenRequest request = new CreateDeviceRegistrationTokenRequest(
                    device.getId(),
                    device.getName(),
                    apiKey
            );

            coreService.createDeviceRegistrationToken(request, new MessageHandlerAdapter<String>() {
                @Override
                public void onMessage(String message) {
                    PlayerConfigurationRequest request = new PlayerConfigurationRequest();
                    request.setDeviceRegistrationToken(message);
                    request.setCloudCoreUrl(coreService.getEndpoint());
                    request.setPlayerName(device.getName());

                    playerService.setPlayerConfiguration(request, new MessageHandlerAdapter<PlayerConfigurationResponse>() {
                        @Override
                        public void onMessage(PlayerConfigurationResponse message) {
                            device.setCloudState(Device.CloudState.REGISTERED);
                        }
                    });
                }
            });
        }
    }

    public void setName(String name) {
        PlayerConfigurationRequest request = new PlayerConfigurationRequest();
        request.setPlayerName(name);
        playerService.setPlayerConfiguration(request, new MessageHandlerAdapter<PlayerConfigurationResponse>() {
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

    public void setPlaybackQueueMode(PlaybackQueueModeRequest request) {
        playerService.setPlaybackQueueMode(request, null);
    }

    public void shuffleTracks() {
        playerService.shuffleTracks(null);
    }

    public void setTrack(Integer playlistPos) {
        playerService.setTrack(playlistPos, null);
    }

    public void removeTracks(PlaybackQueueRemoveTracksRequest request) {
        playerService.removeTracks(request, null);
    }

    public void addTracks(PlaybackQueueAddTracksRequest request) {
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
                    if (message.getPlayerModel() != null) {
                        device.setModel(message.getPlayerModel());
                    }
                    device.setConnectionState(Device.ConnectionState.INITIALIZED);
                }
            });
        }
    }

    public void refreshPlaybackQueue() {
        playerService.getPlaybackQueue(null, new MessageHandlerAdapter<PlaybackQueueResponse>() {
            @Override
            public void onMessage(final PlaybackQueueResponse message) {
                for (MessageHandler<PlaybackQueueResponse> playlistListener : playbackQueueListeners) {
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

    public void addPlaybackQueueListener(MessageHandler<PlaybackQueueResponse> playlistListener) {
        playbackQueueListeners.add(playlistListener);
        if (playbackQueueChangedListener == null) {
            playbackQueueChangedListener = new MessageHandlerAdapter<PlaybackQueueChangedNotification>() {
                @Override
                public void onMessage(PlaybackQueueChangedNotification message) {
                    threadFramework.invoke(new Runnable() {
                        @Override
                        public void run() {
                            refreshPlaybackQueue();
                        }
                    });
                }
            };
            playerService.addPlaybackQueueChangedListener(playbackQueueChangedListener);
        }
    }

    public void removePlaybackQueueListener(MessageHandler<PlaybackQueueResponse> playlistListener) {
        playbackQueueListeners.remove(playlistListener);
        if (playbackQueueListeners.size() == 0 && playbackQueueChangedListener != null) {
            playerService.removePlaybackQueueChangedListener(playbackQueueChangedListener);
            playbackQueueChangedListener = null;
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
