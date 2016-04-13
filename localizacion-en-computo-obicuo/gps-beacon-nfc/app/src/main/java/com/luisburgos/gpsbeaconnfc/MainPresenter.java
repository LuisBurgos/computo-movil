package com.luisburgos.gpsbeaconnfc;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by luisburgos on 9/04/16.
 */
public class MainPresenter implements LocationListener, MainContract.ActionsListener {

    public static float RADIUS_DISTANCE = 100;

    private LocationPreferencesManager mLocationPreferences;
    private MainContract.View mView;

    private LocationManager locationManager;
    private Location mCurrentLocation;

    private Location buildingBLocation;
    private Location campusLibraryLocation;

    public MainPresenter(@NonNull MainContract.View view, @NonNull LocationPreferencesManager locationPreferences) {
        this.mView = view;
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
    public void onLocationChanged(Location locFromGps) {
        // called when the listener is notified with a location update from the GPS
        mCurrentLocation = locFromGps;
        mLocationPreferences.registerLocationValues(locFromGps.getLatitude(), locFromGps.getLongitude());
        mView.setCurrentLocation(
                "LAT" + String.valueOf(locFromGps.getLatitude()) + " - LNG: " + String.valueOf(locFromGps.getLongitude())
        );
        mView.enableDistanceCalculation();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // called when the GPS provider is turned off (user turning off the GPS on the phone)
    }

    @Override
    public void onProviderEnabled(String provider) {
        // called when the GPS provider is turned on (user turning on the GPS on the phone)
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // called when the status of the GPS provider changes
    }

    @Override
    public void calculateDistance() {

        if(mCurrentLocation == null){
            mView.showErrorMessage("Error con ubicación actual");
            return;
        }

        if(!isValidCoordinate(mView.getInputLatitude()) || TextUtils.isEmpty(mView.getInputLatitude()) ){
            mView.setLatitudeErrorMessage();
        }

        if(!isValidCoordinate(mView.getInputLongitude()) || TextUtils.isEmpty(mView.getInputLongitude())){
            mView.setLongitudeErrorMessage();
            return;
        }

        Location targetLocation = getTargetLocation();
        if(targetLocation == null){
            mView.showErrorMessage("Error con ubicación destino");
            return;
        }

        float distance = mCurrentLocation.distanceTo(targetLocation);
        mView.setDistanceBetweenTwoLocations(String.format("%.5f meters", distance));

        if(distance > RADIUS_DISTANCE){
            mView.showErrorMessage("You are not in app radius");
        }
    }

    @Override
    public void loadCurrentLocation() {
        if(mLocationPreferences.hasAlreadySetLocation()){
            mCurrentLocation = new Location("");
            mCurrentLocation.setLatitude(mLocationPreferences.getLatitude());
            mCurrentLocation.setLongitude(mLocationPreferences.getLongitude());
            mView.setCurrentLocation(mLocationPreferences.getLastKnowLocation());
            mView.enableDistanceCalculation();
        }
    }

    private Location getTargetLocation() {
        Location targetLocation = null;
        NumberFormat _format = NumberFormat.getInstance(Locale.US);
        Number number = null;
        double doubleLat = 0, doubleLng = 0;
        try {
            number = _format.parse(mView.getInputLatitude());
            doubleLat = Double.parseDouble(number.toString());
            number = _format.parse(mView.getInputLongitude());
            doubleLng = Double.parseDouble(number.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(doubleLat != 0 || doubleLng != 0){
            targetLocation = new Location("");
            targetLocation.setLatitude(doubleLat);
            targetLocation.setLongitude(doubleLng);

        }

        return targetLocation;
    }

    private boolean isValidCoordinate(String coordinate) {
        boolean valid = coordinate.matches("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");

        Log.d(MainActivity.TAG, coordinate + "is" + String.valueOf(valid));
        return valid;
    }
}
