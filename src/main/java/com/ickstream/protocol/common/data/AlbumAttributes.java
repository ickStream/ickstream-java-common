/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol.common.data;

import java.util.ArrayList;
import java.util.List;

public class AlbumAttributes extends ItemAttributes {
    private String image;
    private List<ArtistAttributes> mainArtists = new ArrayList<ArtistAttributes>();
    private Integer year;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ArtistAttributes> getMainArtists() {
        return mainArtists;
    }

    public void setMainArtists(List<ArtistAttributes> mainArtists) {
        this.mainArtists = mainArtists;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
