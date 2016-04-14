package com.luisburgos.gpsbeaconnfc.network.services;



import com.luisburgos.gpsbeaconnfc.network.APIConstants;
import com.luisburgos.gpsbeaconnfc.network.response.ArticlesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by luisburgos on 5/04/16.
 */
public interface ArticlesApiService {

    @GET(APIConstants.ARTICLES_URL)
    public Call<ArticlesResponse> getArticles();
}
