package com.luisburgos.gpsbeaconnfc.presenters.contracts;

import android.content.Context;

/**
 * Created by luisburgos on 6/02/16.
 */
public interface LoginContract {

    interface View {

        void onLoginResult(Boolean result);

        void setProgressIndicator(boolean active);

        void setCanLoginState(boolean canLoginState);

        void setCurrentLocation(String location);

        void setCurrentDistance(String distance);

        void showLocationSubscribeError();

        void showErrorMessage(String message);
    }

    interface UserActionsListener {

        void subscribeForLocationChanges(Context context);

        void unsubscribeForLocationChanges(Context context);

        void doLogin();

        void reloadInformation();

        void loadLocation();
    }


}
