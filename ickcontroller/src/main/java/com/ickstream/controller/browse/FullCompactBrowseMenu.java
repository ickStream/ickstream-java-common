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

package com.ickstream.controller.browse;

import com.ickstream.controller.service.ServiceController;

import java.util.List;

public class FullCompactBrowseMenu extends FullBrowseMenu {
    public FullCompactBrowseMenu(ServiceController service, List<TypeMenuItem> typeMenusTemplates) {
        super(service, typeMenusTemplates);
    }

    @Override
    public void findContexts(final String language, final ResponseListener<BrowseResponse> listener) {
        super.findContexts(language, new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse browseResponse) {
                if (browseResponse.getItems().size() != 1) {
                    listener.onResponse(browseResponse);
                } else {
                    MenuItem menuItem = browseResponse.getItems().get(0);
                    findItemsInContext(menuItem.getContextId(), language, menuItem.getParent(), listener);
                }
            }
        });
    }

    @Override
    public void findItemsInContext(final String contextId, final String language, final MenuItem menuItem, final ResponseListener<BrowseResponse> listener) {
        super.findItemsInContext(contextId, language, menuItem, new FullBrowseMenu.ResponseListener<BrowseResponse>() {
            @Override
            public void onResponse(BrowseResponse browseResponse) {
                if (browseResponse.getItems().size() != 1 || !(browseResponse.getItems().get(0) instanceof TypeMenuItem)) {
                    listener.onResponse(browseResponse);
                } else {
                    String type = browseResponse.getItems().get(0).getId();
                    findItemsInContextByType(contextId, language, type, menuItem, new FullBrowseMenu.ResponseListener<BrowseResponse>() {
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
