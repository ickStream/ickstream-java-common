/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
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
