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

package com.ickstream.protocol.common.data;

import java.util.ArrayList;
import java.util.List;

public class TrackAttributes extends ItemAttributes {
    private String image;
    private Integer trackNumber;
    private Integer duration;
    private String disc;
    private AlbumAttributes album;
    private List<ArtistAttributes> mainArtists = new ArrayList<ArtistAttributes>();
    private List<ArtistAttributes> composers = new ArrayList<ArtistAttributes>();
    private List<ArtistAttributes> conductors = new ArrayList<ArtistAttributes>();
    private List<ArtistAttributes> performers = new ArrayList<ArtistAttributes>();
    private Integer year;
    private List<CategoryAttributes> categories = new ArrayList<CategoryAttributes>();
    ;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public AlbumAttributes getAlbum() {
        return album;
    }

    public void setAlbum(AlbumAttributes album) {
        this.album = album;
    }

    public List<ArtistAttributes> getMainArtists() {
        return mainArtists;
    }

    public void setMainArtists(List<ArtistAttributes> mainArtists) {
        this.mainArtists = mainArtists;
    }

    public List<ArtistAttributes> getComposers() {
        return composers;
    }

    public void setComposers(List<ArtistAttributes> composers) {
        this.composers = composers;
    }

    public List<ArtistAttributes> getConductors() {
        return conductors;
    }

    public void setConductors(List<ArtistAttributes> conductors) {
        this.conductors = conductors;
    }

    public List<ArtistAttributes> getPerformers() {
        return performers;
    }

    public void setPerformers(List<ArtistAttributes> performers) {
        this.performers = performers;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<CategoryAttributes> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryAttributes> categories) {
        this.categories = categories;
    }
}
