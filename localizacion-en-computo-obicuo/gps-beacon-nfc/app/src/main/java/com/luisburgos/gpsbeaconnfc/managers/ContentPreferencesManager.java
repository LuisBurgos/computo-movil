package com.luisburgos.gpsbeaconnfc.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luisburgos on 26/04/16.
 */
public class ContentPreferencesManager {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;

    int PRIVATE_MODE = 0;

    private static final String CONTENT_PREFERENCES = "CONTENT_PREFERENCES";

    public static final String KEY_IS_ON_CLASSROOM = "KEY_IS_ON_CLASSROOM";
    public static final String KEY_IS_ON_CAMPUS = "KEY_IS_ON_CAMPUS";
    public static final String KEY_IS_OPEN_VIA_NFC = "KEY_IS_OPEN_VIA_NFC";
    private static final String KEY_IS_USER_LOGIN = "IS_USER_LOGIN";


    public ContentPreferencesManager(Context context){
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences(CONTENT_PREFERENCES, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public ContentPreferencesManager registerIsOnCampus(){
        mEditor.putBoolean(KEY_IS_ON_CAMPUS, true);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager registerIsOnClassroom(){
        mEditor.putBoolean(KEY_IS_ON_CLASSROOM, true);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager registerIsOpenViaNFC() {
        mEditor.putBoolean(KEY_IS_OPEN_VIA_NFC, true);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager registerIsUserLogin(){
        mEditor.putBoolean(KEY_IS_USER_LOGIN, true);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager unregisterIsOnCampus(){
        mEditor.putBoolean(KEY_IS_ON_CAMPUS, false);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager unregisterIsOnClassroom(){
        mEditor.putBoolean(KEY_IS_ON_CLASSROOM, false);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager unregisterIsOpenViaNFC(){
        mEditor.putBoolean(KEY_IS_OPEN_VIA_NFC, false);
        mEditor.commit();
        return this;
    }

    public ContentPreferencesManager unregisterIsUserLogin(){
        mEditor.putBoolean(KEY_IS_USER_LOGIN, false);
        mEditor.commit();
        return this;
    }


    public boolean isOnClassroom(){
        return mPreferences.getBoolean(KEY_IS_ON_CLASSROOM, false);
    }

    public boolean isOnCampus(){
        return mPreferences.getBoolean(KEY_IS_ON_CAMPUS, false);
    }


    public boolean isOpenViaNFC(){
        return mPreferences.getBoolean(KEY_IS_OPEN_VIA_NFC, false);
    }

    public boolean isUserLoggedIn(){
        return mPreferences.getBoolean(KEY_IS_USER_LOGIN, false);
    }

    public void clear(){
        mEditor.clear();
        mEditor.commit();
    }
}
