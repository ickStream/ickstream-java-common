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
import com.ickstream.protocol.service.content.AbstractPreferredMenu;

import java.util.List;

public class PreferredTextMenuItem extends AbstractMenuItem implements AbstractPreferredMenuItem {
    public final static String TYPE = "text";
    private AbstractPreferredMenu preferredMenuItem;
    private String text;
    private String id;

    public PreferredTextMenuItem(AbstractPreferredMenu preferredMenuItem, ServiceController serviceController, String text) {
        super(serviceController, null);
        this.preferredMenuItem = preferredMenuItem;
        this.text = text;
        this.id = text;
    }

    public PreferredTextMenuItem(AbstractPreferredMenu preferredMenuItem, ServiceController serviceController, String text, MenuItem parent) {
        super(serviceController, null, parent);
        this.preferredMenuItem = preferredMenuItem;
        this.text = text;
        if (parent != null) {
            this.id = parent.getId() + "." + text;
        } else {
            this.id = text;
        }
    }

    @Override
    public List<String> getPreferredChildItems() {
        return null;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public AbstractPreferredMenu getPreferredMenuItem() {
        return preferredMenuItem;
    }
}
