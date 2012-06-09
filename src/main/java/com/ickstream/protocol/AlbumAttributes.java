/*
 * Copyright (C) 2012 Erland Isaksson (erland@isaksson.info)
 * All rights reserved.
 */

package com.ickstream.protocol;

import java.util.ArrayList;
import java.util.List;

public class AlbumAttributes extends ItemAttributes {
    private String image;
    private List<ArtistAttributes> mainartists = new ArrayList<ArtistAttributes>();
    private Integer year;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<ArtistAttributes> getMainartists() {
        return mainartists;
    }

    public void setMainartists(List<ArtistAttributes> mainartists) {
        this.mainartists = mainartists;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
