package com.luisburgos.studentslogin.presenters;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.luisburgos.studentslogin.utils.UserSessionManager;

/**
 * Created by luisburgos on 10/02/16.
 */
public class MainPresenter implements MainContract.UserActionsListener {

    private MainContract.View mMainView;
    private UserSessionManager sessionManager;

    public MainPresenter(@NonNull MainContract.View mainView) {
        mMainView = mainView;
        sessionManager = new UserSessionManager(((AppCompatActivity)mainView).getApplicationContext());
    }

    @Override
    public void doLogout() {
        sessionManager.logoutUser();
        mMainView.onLogoutResult(true, 0);
    }

    @Override
    public void loadUserInformation() {
        mMainView.showUsername(sessionManager.getUsername());
    }
}
