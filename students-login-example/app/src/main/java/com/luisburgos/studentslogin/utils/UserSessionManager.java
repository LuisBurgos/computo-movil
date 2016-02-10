package com.luisburgos.studentslogin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserSessionManager {

    private SharedPreferences userPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    int PRIVATE_MODE = 0;

    private static final String PREFER_NAME = "userPreferences";
    private static final String IS_USER_LOGIN = "IS_USER_LOGIN";

    public static final String KEY_NAME = "KEY_NAME";
    public static final String KEY_EMAIL = "KEY_EMAIL";

    public UserSessionManager(Context context){
        this.mContext = context;
        userPreferences = mContext.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = userPreferences.edit();
    }

    public void createUserLoginSession(String name, String email){
        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getUsername(){
        return userPreferences.getString(KEY_NAME, null);
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, userPreferences.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, userPreferences.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public boolean isUserLoggedIn(){
        return userPreferences.getBoolean(IS_USER_LOGIN, false);
    }
}