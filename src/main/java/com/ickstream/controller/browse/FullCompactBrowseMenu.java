/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;

import java.util.List;

public class FullCompactBrowseMenu extends FullBrowseMenu {
    public FullCompactBrowseMenu(ServiceController service, List<TypeMenuItem> typeMenusTemplates) {
        super(service, typeMenusTemplates);
    }

    @Override
    public void findContexts(final ResponseListener<BrowseResponse> listener) {
        super.findContexts(new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse browseResponse) {
                if (browseResponse.getItems_loop().size() != 1) {
                    listener.onResponse(browseResponse);
                } else {
                    MenuItem menuItem = browseResponse.getItems_loop().get(0);
                    findItemsInContext(menuItem.getContextId(), menuItem.getParent(), listener);
                }
            }
        });
    }

    @Override
    public void findItemsInContext(final String contextId, final MenuItem menuItem, final ResponseListener<BrowseResponse> listener) {
        super.findItemsInContext(contextId, menuItem, new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse browseResponse) {
                if (browseResponse.getItems_loop().size() != 1 || !(browseResponse.getItems_loop().get(0) instanceof TypeMenuItem)) {
                    listener.onResponse(browseResponse);
                } else {
                    String type = browseResponse.getItems_loop().get(0).getId();
                    findItemsInContextByType(contextId, type, menuItem, new FullBrowseMenu.ResponseListener<BrowseResponse>() {
                        @Override
                        public void onResponse(BrowseResponse response) {
                            listener.onResponse(response);
                        }
                    });
                }
            }
        });
    }
}
