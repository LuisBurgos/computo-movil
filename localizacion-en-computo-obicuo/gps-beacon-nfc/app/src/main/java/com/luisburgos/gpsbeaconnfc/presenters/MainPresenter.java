package com.luisburgos.gpsbeaconnfc.presenters;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.luisburgos.gpsbeaconnfc.interactor.MainInteractor;
import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.network.callbacks.ArticlesCallback;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.MainContract;
import com.luisburgos.gpsbeaconnfc.util.GPSDataLoader;
import com.luisburgos.gpsbeaconnfc.util.Injection;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

/**
 * Created by luisburgos on 9/04/16.
 */
public class MainPresenter implements MainContract.ActionsListener, ArticlesCallback {

    public static float RADIUS_DISTANCE = 200;

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
    public void downloadContent(final Context context) {
        mView.setProgressIndicator(true);
        new GPSDataLoader(context, Injection.provideLocationPreferencesManager(context), new GPSDataLoader.OnLocationLoaded() {
            @Override
            public void onLocationLoadFinished(double lat, double lng) {
                mCurrentLocation = new Location("");
                mCurrentLocation.setLatitude(lat);
                mCurrentLocation.setLongitude(lng);
                mView.setProgressIndicator(false);
                mLocationPreferences.registerLocationValues(lat, lng);
                mView.setCurrentLocation("LAT: " + String.valueOf(lat) + " - LNG: " + String.valueOf(lng));
                Log.d(MainActivity.TAG, "CHANGE LOCATION: " + "LAT: " + String.valueOf(lat) + " - LNG: " + String.valueOf(lng));
                calculateDistance();
            }
        }).loadLastKnownLocation();
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

    public void calculateDistance() {

        if(mCurrentLocation == null){
            mView.showNoLongerInCampusMessage();
            return;
        }

        final Location campusLibraryLocation = new Location("");
        campusLibraryLocation.setLatitude(21.048348);
        campusLibraryLocation.setLongitude(-89.643599);
        float distance = mCurrentLocation.distanceTo(campusLibraryLocation);

        boolean isInsideCampus = distance <= RADIUS_DISTANCE;
        if(!isInsideCampus){
            mView.showNoLongerInCampusMessage();
        } else {
            mInteractor.loadArticlesData(this);
        }
    }
}
