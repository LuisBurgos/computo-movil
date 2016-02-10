package com.luisburgos.studentslogin;

import android.support.annotation.NonNull;

/**
 * Created by luisburgos on 10/02/16.
 */
public class MainPresenter implements MainContract.UserActionsListener {

    private MainContract.View mMainView;

    public MainPresenter(@NonNull MainContract.View mainView) {
        mMainView = mainView;
    }

    @Override
    public void doLogout() {
        mMainView.onLogoutResult(true, 0);
    }
}
