package com.luisburgos.gpsbeaconnfc.network.services;



import com.luisburgos.gpsbeaconnfc.network.ApiConstants;
import com.luisburgos.gpsbeaconnfc.network.model.ArticlesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by luisburgos on 5/04/16.
 */
public interface ArticlesApiService {

    @GET(ApiConstants.ARTICLES_URL)
    public Call<ArticlesResponse> getArticles();
}
