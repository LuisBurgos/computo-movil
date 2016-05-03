package com.luisburgos.gpsbeaconnfc.presenters.contracts;

import android.content.Context;

/**
 * Created by luisburgos on 6/02/16.
 */
public interface LoginContract {

    interface View {

        void onLoginResult(Boolean result);

    }

    interface UserActionsListener {

        void doLogin(Context context);

    }


}
