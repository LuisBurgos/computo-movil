package com.luisburgos.gpsbeaconnfc.util;

import android.location.Location;
import android.util.Log;

import com.luisburgos.gpsbeaconnfc.GPSBeaconNFCApplication;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

/**
 * Created by luisburgos on 29/04/16.
 */
public class LocationHelper {

    //public static float RADIUS_DISTANCE = 200;
    public static float RADIUS_DISTANCE = 8760;

    private Location mCurrentLocation;
    private Location buildingBLocation;
    private Location campusLibraryLocation;

    public LocationHelper() {

        //Edificio B 21.047814, -89.644161
        buildingBLocation = new Location("");
        buildingBLocation.setLatitude(21.047814);
        buildingBLocation.setLongitude(-89.644161);

        //Biblioteca de Ciencias Exactas Mérida, Yuc. 21.048348, -89.643599
        campusLibraryLocation = new Location("");
        campusLibraryLocation.setLatitude(21.048348);
        campusLibraryLocation.setLongitude(-89.643599);
    }

    public float calculateDistanceToCampusLibrary(Location location) {
        if(location == null){
            return -1;
        }
        return location.distanceTo(campusLibraryLocation);
    }

    public boolean isInsideCampus(float distance){
        return distance <= RADIUS_DISTANCE;
    }

    public boolean isValidCoordinate(String coordinate) {
        boolean valid = coordinate.matches("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");
        Log.d(GPSBeaconNFCApplication.TAG, coordinate + "is" + String.valueOf(valid));
        return valid;
    }
}
