package com.luisburgos.gpsbeaconnfc.network.model;

/**
 * Created by luisburgos on 5/04/16.
 */
public interface ArticlesCallback {

    void onDataLoaded(String data);

    void onFailure(String message);

}
