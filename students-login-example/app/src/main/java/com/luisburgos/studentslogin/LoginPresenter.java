package com.luisburgos.studentslogin;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by luisburgos on 6/02/16.
 */
public class LoginPresenter implements LoginContract.UserActionsListener {

    private LoginContract.View mLoginView;
    private Handler handler;

    public LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void doLogin(String username, String password) {

        Boolean isLoginSuccess = true;
        final int code = checkUserValidity(username, password);
        if (code != 0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoginView.onLoginResult(result, code);
            }
        }, 5000);

    }

    private int checkUserValidity(String username, String password) {
        if (username == null || password == null){
            return -1;
        }
        return 0;
    }
}
