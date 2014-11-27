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

package com.ickstream.protocol.service.favorites;

import com.ickstream.common.jsonrpc.*;
import com.ickstream.protocol.common.exception.ServiceException;
import com.ickstream.protocol.common.exception.ServiceTimeoutException;
import com.ickstream.protocol.service.AbstractService;
import com.ickstream.protocol.service.Service;
import org.apache.http.client.HttpClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Client class for accessing Favorites service
 * <p>
 * See the official API documentation for details regarding individual methods and parameters.
 * </p>
 */
public class FavoritesService extends AbstractService implements Service {

    /**
     * Creates a new instance using the specified HttpClient and endpoint URL
     *
     * @param client   The HttpClient instance to use when communicating
     * @param endpoint The endpoint URL to use when communicating
     */
    public FavoritesService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and identity provider
     *
     * @param client     The HttpClient instance to use when communicating
     * @param endpoint   The endpoint URL to use when communicating
     * @param idProvider The identity provider to use when generating JSON-RPC request identities
     */
    public FavoritesService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    /**
     * Creates a new instance using the specified HttpClient, endpoint URL and default timeout
     *
     * @param client         The HttpClient instance to use when communicating
     * @param endpoint       The endpoint URL to use when communicating
     * @param defaultTimeout The default timeout to use if no explicity timeout is specified in the method call
     */
    public FavoritesService(HttpClient client, String endpoint, Integer defaultTimeout) {
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
    public FavoritesService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
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

    public FavoriteItem getFavorite(String favoriteId) throws ServiceException, ServiceTimeoutException {
        return getFavorite(favoriteId, (Integer) null);
    }

    public FavoriteItem getFavorite(String favoriteId, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("id", favoriteId);
            return sendRequest("getFavorite", parameters, FavoriteItem.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void getFavorite(String favoriteId, MessageHandler<FavoriteItem> messageHandler) {
        getFavorite(favoriteId, messageHandler, (Integer) null);
    }

    public void getFavorite(String favoriteId, MessageHandler<FavoriteItem> messageHandler, Integer timeout) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", favoriteId);
        sendRequest("getFavorite", parameters, FavoriteItem.class, messageHandler, timeout);
    }

    public FavoriteItem saveFavorite(FavoriteItem favoriteItem) throws ServiceException, ServiceTimeoutException {
        return saveFavorite(favoriteItem, (Integer) null);
    }

    public FavoriteItem saveFavorite(FavoriteItem favoriteItem, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("saveFavorite", favoriteItem, FavoriteItem.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }

    }

    public void saveFavorite(FavoriteItem libraryItem, MessageHandler<FavoriteItem> messageHandler) {
        saveFavorite(libraryItem, messageHandler, (Integer) null);
    }

    public void saveFavorite(FavoriteItem favoriteItem, MessageHandler<FavoriteItem> messageHandler, Integer timeout) {
        sendRequest("saveFavorite", favoriteItem, FavoriteItem.class, messageHandler, timeout);
    }

    public Boolean removeFavorite(String favoriteId) throws ServiceException, ServiceTimeoutException {
        return removeFavorite(favoriteId, (Integer) null);
    }

    public Boolean removeFavorite(String favoriteId, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            Map<String, String> parameters = new HashMap<String, String>();
            parameters.put("id", favoriteId);
            return sendRequest("removeFavorite", parameters, Boolean.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void removeFavorite(String favoriteId, MessageHandler<Boolean> messageHandler) {
        removeFavorite(favoriteId, messageHandler, (Integer) null);
    }

    public void removeFavorite(String favoriteId, MessageHandler<Boolean> messageHandler, Integer timeout) {
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("id", favoriteId);
        sendRequest("removeFavorite", parameters, Boolean.class, messageHandler, timeout);
    }

    public FavoriteItemResponse findFavorites(FindFavoritesRequest request) throws ServiceException, ServiceTimeoutException {
        return findFavorites(request, (Integer) null);
    }

    public FavoriteItemResponse findFavorites(FindFavoritesRequest request, Integer timeout) throws ServiceException, ServiceTimeoutException {
        try {
            return sendRequest("findFavorites", request, FavoriteItemResponse.class, timeout);
        } catch (JsonRpcException e) {
            throw getServiceException(e);
        } catch (JsonRpcTimeoutException e) {
            throw new ServiceTimeoutException(e);
        }
    }

    public void findFavorites(FindFavoritesRequest request, MessageHandler<FavoriteItemResponse> messageHandler) {
        findFavorites(request, messageHandler, (Integer) null);
    }

    public void findFavorites(FindFavoritesRequest request, MessageHandler<FavoriteItemResponse> messageHandler, Integer timeout) {
        sendRequest("findFavorites", request, FavoriteItemResponse.class, messageHandler, timeout);
    }


}
