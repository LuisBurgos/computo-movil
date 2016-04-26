package com.luisburgos.gpsbeaconnfc.presenters;

import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.luisburgos.gpsbeaconnfc.interactor.MainInteractor;
import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.network.callbacks.ArticlesCallback;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.MainContract;
import com.luisburgos.gpsbeaconnfc.util.Injection;

/**
 * Created by luisburgos on 9/04/16.
 */
public class MainPresenter implements MainContract.ActionsListener, ArticlesCallback {

    public static float RADIUS_DISTANCE = 100;

    private LocationPreferencesManager mLocationPreferences;
    private MainContract.View mView;

    private MainInteractor mInteractor;
    private LocationManager locationManager;
    private Location mCurrentLocation;

    public MainPresenter(
            @NonNull MainContract.View view,
            @NonNull LocationPreferencesManager locationPreferences,
            @NonNull MainInteractor interactor
    ) {
        this.mView = view;
        mLocationPreferences = locationPreferences;
        mInteractor = interactor;
    }

    @Override
    public void downloadContent() {
        mView.setProgressIndicator(true);
        mInteractor.loadArticlesData(this);
    }

    @Override
    public void onDataLoaded(String data) {
        mView.setProgressIndicator(false);
        mView.showContent(data);
    }

    @Override
    public void onFailure(String message) {
        mView.setProgressIndicator(false);
        mView.showErrorMessage();
    }
}
