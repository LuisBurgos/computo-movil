package com.luisburgos.studentslogin.presenters;

/**
 * Created by luisburgos on 10/02/16.
 */
public interface MainContract {

    interface View {

        void onLogoutResult(Boolean result, int code);

        void showConfirmationDialog();

        void showUsername(String username);

        void setProgressIndicator(boolean active);

    }

    interface UserActionsListener {

        void doLogout();

        void loadUserInformation();
    }

}
