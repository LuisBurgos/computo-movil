package com.luisburgos.gpsbeaconnfc.presenters;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.MainContract;
import com.luisburgos.gpsbeaconnfc.util.PermissionHelper;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by luisburgos on 9/04/16.
 */
public class MainPresenter implements MainContract.ActionsListener {

    public static float RADIUS_DISTANCE = 100;

    private LocationPreferencesManager mLocationPreferences;
    private MainContract.View mView;

    private LocationManager locationManager;
    private Location mCurrentLocation;

    public MainPresenter(@NonNull MainContract.View view, @NonNull LocationPreferencesManager locationPreferences) {
        this.mView = view;
        mLocationPreferences = locationPreferences;
    }

}
