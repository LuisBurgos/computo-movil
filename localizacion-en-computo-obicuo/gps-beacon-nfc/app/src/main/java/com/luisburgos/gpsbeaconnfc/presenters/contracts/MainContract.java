package com.luisburgos.gpsbeaconnfc.presenters.contracts;

import android.content.Context;

/**
 * Created by luisburgos on 9/04/16.
 */
public interface MainContract {

    interface View {

        void showContent(String content);

        void setProgressIndicator(boolean active);

        void showErrorMessage();

        void setCurrentLocation(String location);

        void showNoLongerInCampusMessage();
    }

    interface ActionsListener {

        void downloadContent(Context context);
    }

}
