package com.luisburgos.gpsbeaconnfc.presenters;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.luisburgos.gpsbeaconnfc.GPSBeaconNFCApplication;
import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.network.interactor.MainInteractor;
import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.network.callbacks.ArticlesCallback;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.MainContract;
import com.luisburgos.gpsbeaconnfc.util.Injection;
import com.luisburgos.gpsbeaconnfc.util.LocationHelper;
import com.luisburgos.gpsbeaconnfc.util.PermissionHelper;

/**
 * Created by luisburgos on 9/04/16.
 */
public class MainPresenter implements MainContract.ActionsListener, LocationListener {

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
        mCurrentLocation = new Location("");
        mCurrentLocation.setLatitude(Injection.provideLocationPreferencesManager(context).getLatitude());
        mCurrentLocation.setLongitude(Injection.provideLocationPreferencesManager(context).getLongitude());
        calculateDistance();
    }

    public void calculateDistance() {

        if(mCurrentLocation == null){
            mView.showErrorMessage("Error al obtener tu ubicación");
            return;
        }

        LocationHelper helper = new LocationHelper();
        float distance = helper.calculateDistanceToCampusLibrary(mCurrentLocation);
        mView.setCurrentDistance(String.format("%.5f metros", distance));

        boolean isInsideCampus = helper.isInsideCampus(distance);
        mView.setCanLoginState(isInsideCampus);

        if(!isInsideCampus){
            mView.showNoLongerInCampusMessage();
        } else {
            //mInteractor.loadArticlesData(this);
        }
    }

    @Override
    public void loadLocation(final Context context) {
        if(mLocationPreferences.hasAlreadySetLocation()){
            mCurrentLocation = new Location("");
            mCurrentLocation.setLatitude(mLocationPreferences.getLatitude());
            mCurrentLocation.setLongitude(mLocationPreferences.getLongitude());
            mView.setCurrentLocation(mLocationPreferences.getLastKnowLocation());
        }
        subscribeForLocationChanges(context);
    }

    @Override
    public void subscribeForLocationChanges(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (PermissionHelper.hasNoPermissionToAccessLocation(context)) {
            mView.showLocationSubscribeError();
            mView.setProgressIndicator(false);
            return;
        }

        Criteria criteria = new Criteria();
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);

        locationManager.requestLocationUpdates(locationManager.getBestProvider(criteria, true), 2000, 0, this);
        mView.setProgressIndicator(true);
    }

    @Override
    public void unsubscribeForLocationChanges(Context context) {
        locationManager.removeUpdates(this);
        mView.setProgressIndicator(false);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLocationPreferences.registerLocationValues(location.getLatitude(), location.getLongitude());
        mView.setCurrentLocation(
                "LAT: " + String.valueOf(location.getLatitude()) + " - LNG: " + String.valueOf(location.getLongitude())
        );
        Log.d(GPSBeaconNFCApplication.TAG, "GPS LOCATION: " +
                "LAT: " + String.valueOf(location.getLatitude()) + " - LNG: " + String.valueOf(location.getLongitude()));
        mView.setProgressIndicator(false);
        calculateDistance();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(GPSBeaconNFCApplication.TAG, "STATUS CHANGED: " + provider);
        mView.setProgressIndicator(false);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(GPSBeaconNFCApplication.TAG, "ENABLED: " + provider);
        mView.setProgressIndicator(false);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(GPSBeaconNFCApplication.TAG, "DISABLED: " + provider);
        mView.setProgressIndicator(false);
    }

    public void doLogin() {
        mView.onLoginResult(true);
    }
}
