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

package com.ickstream.protocol.service.content;

import com.fasterxml.jackson.databind.JsonNode;

public class SelectionParameters {
    public static final String TYPE_RANDOM_ALL = "RANDOM_ALL";
    public static final String TYPE_RANDOM_MY_LIBRARY = "RANDOM_MY_LIBRARY";
    public static final String TYPE_RANDOM_MY_PLAYLISTS = "RANDOM_MY_PLAYLISTS";
    public static final String TYPE_RANDOM_FRIENDS = "RANDOM_FRIENDS";
    public static final String TYPE_RANDOM_FOR_ARTIST = "RANDOM_FOR_ARTIST";
    public static final String TYPE_RANDOM_FOR_PLAYLIST = "RANDOM_FOR_PLAYLIST";
    public static final String TYPE_RANDOM_FOR_CATEGORY = "RANDOM_FOR_CATEGORY";

    private String type;
    private JsonNode data;

    public SelectionParameters() {
    }

    public SelectionParameters(String type) {
        this.type = type;
    }

    public SelectionParameters(String type, JsonNode data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getData() {
        return data;
    }

    public void setData(JsonNode data) {
        this.data = data;
    }
}
