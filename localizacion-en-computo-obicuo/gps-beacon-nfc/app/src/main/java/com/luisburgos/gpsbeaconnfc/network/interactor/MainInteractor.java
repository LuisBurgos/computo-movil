package com.luisburgos.gpsbeaconnfc.network.interactor;

import android.util.Log;

import com.google.gson.Gson;
import com.luisburgos.gpsbeaconnfc.GPSBeaconNFCApplication;
import com.luisburgos.gpsbeaconnfc.network.callbacks.ArticlesCallback;
import com.luisburgos.gpsbeaconnfc.network.response.ArticlesResponse;
import com.luisburgos.gpsbeaconnfc.network.services.ArticlesApiService;
import com.luisburgos.gpsbeaconnfc.network.services.ServiceGenerator;
import com.luisburgos.gpsbeaconnfc.util.files.JSONHelper;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by luisburgos on 19/04/16.
 */
public class MainInteractor {

    ArticlesApiService articlesApiService = ServiceGenerator.createService(ArticlesApiService.class);

    public void loadArticlesData(final ArticlesCallback callback) {
        Call<ArticlesResponse> call = articlesApiService.getArticles();
        Log.d(GPSBeaconNFCApplication.TAG, "ORIGINAL URL LOGIN REQ: " + call.request().toString());
        call.enqueue(new Callback<ArticlesResponse>() {

            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {

                if (response.isSuccessful()) {
                    ArticlesResponse articlesResponse = response.body();
                    String JSONString = new Gson().toJson(articlesResponse);
                    JSONString = JSONHelper.prettyFormat(JSONString);
                    Log.d(GPSBeaconNFCApplication.TAG, "DATA LOADED: " + JSONString);
                    callback.onDataLoaded(JSONString);
                } else {
                    callback.onFailure("Error en la descarga");
                }
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                callback.onFailure("Problema de Red");
            }
        });
    }

}
