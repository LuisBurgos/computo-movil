package com.luisburgos.gpsbeaconnfc.presenters;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.managers.UserSessionManager;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.LoginContract;
import com.luisburgos.gpsbeaconnfc.util.PermissionHelper;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by luisburgos on 6/02/16.
 */
public class LoginPresenter implements LocationListener, LoginContract.UserActionsListener {

    public static float RADIUS_DISTANCE = 100;

    private LoginContract.View mView;

    private LocationPreferencesManager mLocationPreferences;
    private LocationManager locationManager;
    private Location mCurrentLocation;
    private Location buildingBLocation;
    private Location campusLibraryLocation;

    private UserSessionManager mSessionManager;

    public LoginPresenter(
            LoginContract.View view,
            UserSessionManager sessionManager,
            LocationPreferencesManager locationPreferences
    ) {
        this.mView = view;
        mSessionManager = sessionManager;
        mLocationPreferences = locationPreferences;

        //Edificio B 21.047814, -89.644161
        buildingBLocation = new Location("");
        buildingBLocation.setLatitude(21.047814);
        buildingBLocation.setLongitude(-89.644161);

        //Biblioteca de Ciencias Exactas Mérida, Yuc. 21.048348, -89.643599
        campusLibraryLocation = new Location("");
        campusLibraryLocation.setLatitude(21.048348);
        campusLibraryLocation.setLongitude(-89.643599);
    }

    @Override
    public void subscribeForLocationChanges(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (PermissionHelper.hasNoPermissionToAccessLocation(context)) {
            mView.showLocationSubscribeError();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 35000, 10, this);
    }

    @Override
    public void unsubscribeForLocationChanges(Context context) {
        locationManager.removeUpdates(this);
    }

    @Override
    public void doLogin() {
        //mSessionManager.createUserLoginSession("", "");
        mView.showMain();
    }

    @Override
    public void reloadInformation() {

    }

    @Override
    public void loadLocation() {
        if(mLocationPreferences.hasAlreadySetLocation()){
            mCurrentLocation = new Location("");
            mCurrentLocation.setLatitude(mLocationPreferences.getLatitude());
            mCurrentLocation.setLongitude(mLocationPreferences.getLongitude());
            mView.setCurrentLocation(mLocationPreferences.getLastKnowLocation());
            calculateDistance();
        } else{
            mView.setCanLoginState(false);
        }
    }

    public void calculateDistance() {

        if(mCurrentLocation == null){
            mView.showErrorMessage("Error con ubicación actual");
            return;
        }

        float distance = mCurrentLocation.distanceTo(campusLibraryLocation);
        mView.setCurrentDistance(String.format("%.5f metros", distance));

        boolean isInsideCampus = distance <= RADIUS_DISTANCE;
        mView.setCanLoginState(isInsideCampus);
        if(!isInsideCampus){
            mView.showErrorMessage("No estás dentro del campus");
        }
    }

    private boolean isValidCoordinate(String coordinate) {
        boolean valid = coordinate.matches("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");
        Log.d(MainActivity.TAG, coordinate + "is" + String.valueOf(valid));
        return valid;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLocationPreferences.registerLocationValues(location.getLatitude(), location.getLongitude());
        mView.setCurrentLocation(
                "LAT: " + String.valueOf(location.getLatitude()) + " - LNG: " + String.valueOf(location.getLongitude())
        );
        Log.d(MainActivity.TAG, "CHANGE LOCATION: " +
                "LAT: " + String.valueOf(location.getLatitude()) + " - LNG: " + String.valueOf(location.getLongitude()));
        calculateDistance();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(MainActivity.TAG, "ENABLED: " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(MainActivity.TAG, "ENABLED: " + provider);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(MainActivity.TAG, "DISABLED: " + provider);
    }
}
