package com.luisburgos.gpsbeaconnfc.util;

import android.content.Context;

import com.luisburgos.gpsbeaconnfc.network.interactor.MainInteractor;
import com.luisburgos.gpsbeaconnfc.managers.ContentPreferencesManager;
import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;

/**
 * Created by luisburgos on 12/04/16.
 */
public class Injection {

    public static LocationPreferencesManager provideLocationPreferencesManager(Context context) {
        return new LocationPreferencesManager(context);
    }

    public static MainInteractor provideMainInteractor() {
        return new MainInteractor();
    }

    public static ContentPreferencesManager provideContentPreferencesManager(Context context) {
        return new ContentPreferencesManager(context);
    }
}
