/*
 * Copyright (C) 2013 ickStream GmbH
 * All rights reserved
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

public class FavoritesService extends AbstractService implements Service {

    public FavoritesService(HttpClient client, String endpoint) {
        this(client, endpoint, (Integer) null);
    }

    public FavoritesService(HttpClient client, String endpoint, IdProvider idProvider) {
        this(client, endpoint, idProvider, null);
    }

    public FavoritesService(HttpClient client, String endpoint, Integer defaultTimeout) {
        this(client, endpoint, null, defaultTimeout);
    }

    public FavoritesService(HttpClient client, String endpoint, IdProvider idProvider, Integer defaultTimeout) {
        super(new HttpMessageSender(client, endpoint, true), idProvider, defaultTimeout);
        ((HttpMessageSender) getMessageSender()).setResponseHandler(this);
    }

    public void setMessageLogger(MessageLogger messageLogger) {
        ((HttpMessageSender) getMessageSender()).setMessageLogger(messageLogger);
    }

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
