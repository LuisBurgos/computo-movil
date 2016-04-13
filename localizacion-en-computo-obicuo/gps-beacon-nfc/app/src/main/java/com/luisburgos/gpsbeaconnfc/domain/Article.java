package com.luisburgos.gpsbeaconnfc.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by luisburgos on 5/04/16.
 */
public class Article {

    @SerializedName("title")
    String title;

    @SerializedName("url")
    String url;

    @SerializedName("categories")
    List categories;

    @SerializedName("tags")
    List tags;

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public List getCategories() {
        return categories;
    }

    public List getTags() {
        return tags;
    }
}
