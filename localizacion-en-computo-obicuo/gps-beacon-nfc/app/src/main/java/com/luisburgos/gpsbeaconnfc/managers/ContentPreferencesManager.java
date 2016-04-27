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

    public ContentPreferencesManager(Context context){
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences(CONTENT_PREFERENCES, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public void registerIsOnCampus(){
        mEditor.putBoolean(KEY_IS_ON_CAMPUS, true);
        mEditor.commit();
    }

    public void registerIsOnClassroom(){
        mEditor.putBoolean(KEY_IS_ON_CLASSROOM, true);
        mEditor.commit();
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


    public boolean isOnClassroom(){
        return mPreferences.getBoolean(KEY_IS_ON_CLASSROOM, false);
    }

    public boolean isOnCampus(){
        return mPreferences.getBoolean(KEY_IS_ON_CAMPUS, false);
    }

    public void clear(){
        mEditor.clear();
        mEditor.commit();
    }
}
