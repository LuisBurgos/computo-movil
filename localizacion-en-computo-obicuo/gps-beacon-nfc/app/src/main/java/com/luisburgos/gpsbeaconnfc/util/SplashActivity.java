package com.luisburgos.gpsbeaconnfc.util;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;
import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.views.activities.LoginActivity;
import com.luisburgos.gpsbeaconnfc.managers.UserSessionManager;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final UserSessionManager sessionManager = new UserSessionManager(SplashActivity.this);

        TextView mMessage = (TextView) findViewById(R.id.splash_message);
        Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/lobster.otf");
        if(mMessage != null){
            mMessage.setTypeface(robotoBoldCondensedItalic);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(sessionManager.isUserLoggedIn()){
                    i = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
