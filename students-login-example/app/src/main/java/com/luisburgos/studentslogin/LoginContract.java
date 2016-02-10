package com.luisburgos.studentslogin;

/**
 * Created by luisburgos on 6/02/16.
 */
public interface LoginContract {

    interface View {

        void onLoginResult(Boolean result, int code);

        void setProgressIndicator(boolean active);

        void showEmptyDataMessage();

        void showInvalidEmailMessage();

        void showIncorrectPasswordMessage();

        void showLoginFailedMessage();

    }

    interface UserActionsListener {

        void doLogin(String username, String password);

    }


}
