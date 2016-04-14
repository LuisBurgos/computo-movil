package com.luisburgos.gpsbeaconnfc.presenters;

import android.content.Context;

/**
 * Created by luisburgos on 9/04/16.
 */
public interface MainContract {

    interface View {

        void setCurrentLocation(String location);

        void setDistanceBetweenTwoLocations(String distanceBetweenTwoLocations);

        void showLocationSubscribeError();

        void showErrorMessage(String message);

        String getInputLatitude();

        String getInputLongitude();

        void setLongitudeErrorMessage();

        void setLatitudeErrorMessage();

        void enableDistanceCalculation();
    }

    interface ActionsListener {

        void subscribeForLocationChanges(Context context);

        void unsubscribeForLocationChanges(Context context);

        void calculateDistance();

        void loadCurrentLocation();

    }

}
