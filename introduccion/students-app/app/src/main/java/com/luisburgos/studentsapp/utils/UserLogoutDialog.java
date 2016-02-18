package com.luisburgos.studentsapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.login.LoginActivity;

/**
 * Created by luisburgos on 17/02/16.
 */
public class UserLogoutDialog  {

    private Activity mActivity;
    private AlertDialog.Builder alertDialogBuilder;

    public UserLogoutDialog(Activity activity) {
        mActivity = activity;
        init();
    }

    private void init(){
        alertDialogBuilder = new AlertDialog.Builder(mActivity);
        alertDialogBuilder.setMessage(mActivity.getResources().getString(R.string.logout_confirm_message));
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                new UserSessionManager(mActivity).logoutUser();
                mActivity.finish();
                mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
    }

    public void show() {
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
