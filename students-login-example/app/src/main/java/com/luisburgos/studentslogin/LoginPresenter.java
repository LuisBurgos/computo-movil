package com.luisburgos.studentslogin;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by luisburgos on 6/02/16.
 */
public class LoginPresenter implements LoginContract.UserActionsListener {

    private LoginContract.View mLoginView;
    private Handler handler;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    public LoginPresenter(LoginContract.View mLoginView) {
        this.mLoginView = mLoginView;
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void doLogin(String username, String password) {
        mLoginView.setProgressIndicator(true);
        if(validateDataLogin(username, password)){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoginView.onLoginResult(true, 0);
                }
            }, 3000);
        }else {
            mLoginView.setProgressIndicator(false);
        }

    }

    private int checkUserValidity(String username, String password) {
        if (username == null || password == null){
            return -1;
        }
        return 0;
    }

    private boolean validateEmail(String email) {
        //matcher = pattern.matcher(email);
        //return matcher.matches();
        return true;
    }

    private boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private boolean validateDataLogin(String username, String password) {

        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)){
            mLoginView.showEmptyDataMessage();
            return false;
        }

        if (!validateEmail(username)) {
            mLoginView.showInvalidEmailMessage();
            return false;
        } else if (!validatePassword(password)) {
            mLoginView.showIncorrectPasswordMessage();
            return false;
        } else {
            return true;
        }
    }
}
