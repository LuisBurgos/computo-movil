package com.luisburgos.studentsapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luisburgos on 23/02/16.
 */
public class UserSettingsManager {

    private SharedPreferences settingsPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    int PRIVATE_MODE = 0;

    private static final String SETTINGS_PREFERENCES = "settingsPreferences";

    private static final String IS_STUDENTS_DATABASE_INIT = "IS_STUDENTS_DATABASE_INIT";
    private static final String IS_USERS_DATABASE_INIT = "IS_USERS_DATABASE_INIT";

    public UserSettingsManager(Context context){
        this.mContext = context;
        settingsPreferences = mContext.getSharedPreferences(SETTINGS_PREFERENCES, PRIVATE_MODE);
        editor = settingsPreferences.edit();
    }

    public void registerStudentsDatabaseInit(){
        editor.putBoolean(IS_STUDENTS_DATABASE_INIT, true);
        editor.commit();
    }

    public void registerUsersDatabaseInit(){
        editor.putBoolean(IS_USERS_DATABASE_INIT, true);
        editor.commit();
    }

    public boolean isUsersDatabaseInit(){
        return settingsPreferences.getBoolean(IS_USERS_DATABASE_INIT, false);
    }

    public boolean isStudentsDatabaseInit(){
        return settingsPreferences.getBoolean(IS_STUDENTS_DATABASE_INIT, false);
    }

}
