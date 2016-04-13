package com.luisburgos.gpsbeaconnfc.network.model;

import com.google.gson.annotations.SerializedName;
import com.luisburgos.gpsbeaconnfc.domain.Article;

import java.util.ArrayList;

/**
 * Created by luisburgos on 5/04/16.
 */
public class ArticlesResponse {

    @SerializedName("articleList")
    ArrayList<Article> articleList;

    public ArrayList<Article> getArticleList() {
        return articleList;
    }
}
