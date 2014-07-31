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

package com.ickstream.protocol.service.playlists;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.Service;
import org.apache.http.client.HttpClient;

/**
 * Client class for accessing Playlists service
 * <p/>
 * See the official API documentation for details regarding individual methods and parameters.
 */
public class PlaylistsService extends AbstractService implements Service {

    /**
     * Creates a new instance using the specified HttpClient and endpoint URL
     *
     * @param client   The HttpClient instance to use when communicating
     * @param endpoint The endpoint URL to use when communicating
     */
    public PlaylistsService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and identity provider
     *
     * @param client     The HttpClient instance to use when communicating
     * @param endpoint   The endpoint URL to use when communicating
     * @param idProvider The identity provider to use when generating JSON-RPC request identities
     */
    public PlaylistsService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public PlaylistsService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL, identity provider and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param idProvider     The identity provider to use when generating JSON-RPC request identities
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public PlaylistsService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    /**
     * Set message logging to use to log incoming and outgoing messages handled by this client
     *
     * @param messageLogger A message logger implementation
     */
    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

    /**
     * Set access token to use for authorization
     *
     * @param accessToken OAuth access token to use for authorization
     */
    public void setAccessToken(String accessToken) {
        ((HttpMessageSender) getMessageSender()).setAccessToken(accessToken);
    }

    public FindPlaylistsResponse findPlaylists(FindPlaylistsRequest request) throws ServiceException, ServiceTimeoutException {
        return findPlaylists(request, (Integer) null);
    }

    public FindPlaylistsResponse findPlaylists(FindPlaylistsRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findPlaylists", request, FindPlaylistsResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findPlaylists(FindPlaylistsRequest request, MessageHandler<FindPlaylistsResponse> messageHandler) {
        findPlaylists(request, messageHandler, (Integer) null);
    }

    public void findPlaylists(FindPlaylistsRequest request, MessageHandler<FindPlaylistsResponse> messageHandler, Integer timeout) {
        sendRequest("findPlaylists", request, FindPlaylistsResponse.class, messageHandler, timeout);
    }

    public GetPlaylistResponse getPlaylist(GetPlaylistRequest request) throws ServiceException, ServiceTimeoutException {
        return getPlaylist(request, (Integer) null);
    }

    public GetPlaylistResponse getPlaylist(GetPlaylistRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("getPlaylist", request, GetPlaylistResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getPlaylist(GetPlaylistRequest request, MessageHandler<GetPlaylistResponse> messageHandler) {
        getPlaylist(request, messageHandler, (Integer) null);
    }

    public void getPlaylist(GetPlaylistRequest request, MessageHandler<GetPlaylistResponse> messageHandler, Integer timeout) {
        sendRequest("getPlaylist", request, GetPlaylistResponse.class, messageHandler, timeout);
    }

    public Boolean removePlaylist(RemovePlaylistRequest request) throws ServiceException, ServiceTimeoutException {
        return removePlaylist(request, (Integer) null);
    }

    public Boolean removePlaylist(RemovePlaylistRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("removePlaylist", request, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removePlaylist(RemovePlaylistRequest request, MessageHandler<Boolean> messageHandler) {
        removePlaylist(request, messageHandler, (Integer) null);
    }

    public void removePlaylist(RemovePlaylistRequest request, MessageHandler<Boolean> messageHandler, Integer timeout) {
        sendRequest("removePlaylist", request, Boolean.class, messageHandler, timeout);
    }

    public SetPlaylistNameResponse setPlaylistName(SetPlaylistNameRequest request) throws ServiceException, ServiceTimeoutException {
        return setPlaylistName(request, (Integer) null);
    }

    public SetPlaylistNameResponse setPlaylistName(SetPlaylistNameRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setPlaylistName", request, SetPlaylistNameResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void setPlaylistName(SetPlaylistNameRequest request, MessageHandler<SetPlaylistNameResponse> messageHandler) {
        setPlaylistName(request, messageHandler, (Integer) null);
    }

    public void setPlaylistName(SetPlaylistNameRequest request, MessageHandler<SetPlaylistNameResponse> messageHandler, Integer timeout) {
        sendRequest("setPlaylistName", request, SetPlaylistNameResponse.class, messageHandler, timeout);
    }

    public PlaylistModificationResponse setTracks(PlaylistSetTracksRequest request) throws ServiceException, ServiceTimeoutException {
        return setTracks(request, (Integer) null);
    }

    public PlaylistModificationResponse setTracks(PlaylistSetTracksRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("setTracks", request, PlaylistModificationResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void setTracks(PlaylistSetTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        setTracks(request, messageHandler, (Integer) null);
    }

    public void setTracks(PlaylistSetTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("setTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public PlaylistModificationResponse addTracks(PlaylistAddTracksRequest request) throws ServiceException, ServiceTimeoutException {
        return addTracks(request, (Integer) null);
    }

    public PlaylistModificationResponse addTracks(PlaylistAddTracksRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("addTracks", request, PlaylistModificationResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        addTracks(request, messageHandler, (Integer) null);
    }

    public void addTracks(PlaylistAddTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("addTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public PlaylistModificationResponse removeTracks(PlaylistRemoveTracksRequest request) throws ServiceException, ServiceTimeoutException {
        return removeTracks(request, (Integer) null);
    }

    public PlaylistModificationResponse removeTracks(PlaylistRemoveTracksRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("removeTracks", request, PlaylistModificationResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        removeTracks(request, messageHandler, (Integer) null);
    }

    public void removeTracks(PlaylistRemoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("removeTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }

    public PlaylistModificationResponse moveTracks(PlaylistMoveTracksRequest request) throws ServiceException, ServiceTimeoutException {
        return moveTracks(request, (Integer) null);
    }

    public PlaylistModificationResponse moveTracks(PlaylistMoveTracksRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("moveTracks", request, PlaylistModificationResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void moveTracks(PlaylistMoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler) {
        moveTracks(request, messageHandler, (Integer) null);
    }

    public void moveTracks(PlaylistMoveTracksRequest request, MessageHandler<PlaylistModificationResponse> messageHandler, Integer timeout) {
        sendRequest("moveTracks", request, PlaylistModificationResponse.class, messageHandler, timeout);
    }
}
