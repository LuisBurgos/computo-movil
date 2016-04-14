package com.luisburgos.gpsbeaconnfc.util;

import android.content.Context;

import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.managers.UserSessionManager;
import com.luisburgos.gpsbeaconnfc.views.activities.LoginActivity;

/**
 * Created by luisburgos on 12/04/16.
 */
public class Injection {
    public static UserSessionManager provideUserSessionManager(Context context) {
        return new UserSessionManager(context);
    }

    public static LocationPreferencesManager provideLocationPreferencesManager(Context context) {
        return new LocationPreferencesManager(context);
    }
}
