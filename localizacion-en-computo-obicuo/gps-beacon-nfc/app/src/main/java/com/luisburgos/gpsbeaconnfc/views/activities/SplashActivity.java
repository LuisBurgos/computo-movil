package com.luisburgos.gpsbeaconnfc.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.managers.ContentPreferencesManager;
import com.luisburgos.gpsbeaconnfc.managers.UserSessionManager;
import com.luisburgos.gpsbeaconnfc.presenters.LoginPresenter;
import com.luisburgos.gpsbeaconnfc.util.GPSDataLoader;
import com.luisburgos.gpsbeaconnfc.util.Injection;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    private TextView message;
    private ProgressDialog mProgressDialog;
    private UserSessionManager sessionManager;
    private ContentPreferencesManager contentPreferencesManager;
    private Location mCurrentLocation;
    private Location campusLibraryLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new UserSessionManager(SplashActivity.this);
        contentPreferencesManager = Injection.provideContentPreferencesManager(this);
        //sessionManager.logoutUser();

        campusLibraryLocation = new Location("");
        campusLibraryLocation.setLatitude(21.048348);
        campusLibraryLocation.setLongitude(-89.643599);

        TextView mMessage = (TextView) findViewById(R.id.splash_message);
        Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/lobster.otf");
        if(mMessage != null){
            mMessage.setTypeface(robotoBoldCondensedItalic);
        }

        setupProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mProgressDialog.show();
        new GPSDataLoader(this, Injection.provideLocationPreferencesManager(this), new GPSDataLoader.OnLocationLoaded() {
            @Override
            public void onLocationLoadFinished(double lat, double lng) {
                mProgressDialog.dismiss();
                Log.d(MainActivity.TAG, "CHANGE LOCATION: " + "LAT: " + String.valueOf(lat) + " - LNG: " + String.valueOf(lng));
                mCurrentLocation = new Location("");
                mCurrentLocation.setLatitude(lat);
                mCurrentLocation.setLongitude(lng);
                calculateDistance();
            }
        }).loadLastKnownLocation();

        /*new Handler().postDelayed(new Runnable() {
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
        }, SPLASH_TIME_OUT);*/
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(SplashActivity.this);
        mProgressDialog.setMessage("Verificando datos");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    public void calculateDistance() {

        float distance = mCurrentLocation.distanceTo(campusLibraryLocation);
        boolean isInsideCampus = distance <= LoginPresenter.RADIUS_DISTANCE;

        Log.d(MainActivity.TAG, "DEVICE ON CAMPUS: " + isInsideCampus);
        Intent i;
        if(isInsideCampus){
            i = new Intent(SplashActivity.this, MainActivity.class);
            if(contentPreferencesManager.isOnClassroom()){
                i.putExtra(MainActivity.CAN_DOWNLOAD_CONTENT, true);
            }
        } else {
            i = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(i);
        finish();
    }
}
