package com.luisburgos.gpsbeaconnfc.presenters.contracts;

import android.content.Context;

/**
 * Created by luisburgos on 9/04/16.
 */
public interface MainContract {

    interface View {

        void showContent(String content);

        void showNoLongerInCampusMessage();

        void setProgressIndicator(boolean active);

        void setCanLoginState(boolean canLoginState);

        void setCurrentLocation(String location);

        void setCurrentDistance(String distance);

        void showLocationSubscribeError();

        void showErrorMessage(String message);

        void onLoginResult(boolean result);
    }

    interface ActionsListener {

        void subscribeForLocationChanges(Context context);

        void unsubscribeForLocationChanges(Context context);

        void loadLocation(Context context);

        void downloadContent(Context context);
    }

}
