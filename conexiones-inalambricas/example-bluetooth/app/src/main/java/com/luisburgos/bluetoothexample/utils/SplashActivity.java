package com.luisburgos.bluetoothexample.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.luisburgos.bluetoothexample.listdevices.BluetoothDevicesActivity;
import com.luisburgos.bluetoothexample.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //final UserSessionManager sessionManager = new UserSessionManager(SplashActivity.this);

        TextView mMessage = (TextView) findViewById(R.id.splash_message);
        Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/lobster.otf");
        if(mMessage != null){
            mMessage.setTypeface(robotoBoldCondensedItalic);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isBluetoothSupported()){
                    Intent i;
                    //if(sessionManager.isUserLoggedIn()){
                    //i = new Intent(SplashActivity.this, StudentsActivity.class);
                    //}else{
                    i = new Intent(SplashActivity.this, BluetoothDevicesActivity.class);
                    //}
                    startActivity(i);

                }else{
                    showBluetoothUnsupportedMessage();
                };
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }

    private void showBluetoothUnsupportedMessage(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.device_not_compatible))
                .setMessage(getString(R.string.device_not_compatible_explanation))
                .setPositiveButton(getString(R.string.device_not_compatible_action),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
