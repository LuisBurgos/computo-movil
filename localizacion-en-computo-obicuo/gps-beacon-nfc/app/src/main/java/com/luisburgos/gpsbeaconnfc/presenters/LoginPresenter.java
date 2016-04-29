package com.luisburgos.gpsbeaconnfc.presenters;

import android.content.Context;
import android.location.LocationManager;

import com.luisburgos.gpsbeaconnfc.managers.ContentPreferencesManager;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.LoginContract;


/**
 * Created by luisburgos on 6/02/16.
 */
public class LoginPresenter implements LoginContract.UserActionsListener {

    private LoginContract.View mView;

    private ContentPreferencesManager mContentPreferencesManager;
    private LocationManager locationManager;

    public LoginPresenter(LoginContract.View view, ContentPreferencesManager contentPreferencesManager) {
        this.mView = view;
        mContentPreferencesManager = contentPreferencesManager;
    }

    @Override
    public void doLogin(Context context) {
        mContentPreferencesManager.registerIsUserLogin();
        mView.onLoginResult(true);
    }

}
