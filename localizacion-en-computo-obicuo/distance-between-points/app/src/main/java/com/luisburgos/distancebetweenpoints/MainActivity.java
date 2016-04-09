package com.luisburgos.distancebetweenpoints;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTextView = (TextView) findViewById(R.id.locationTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GPSDataLoader(this, new LocationPreferencesManager(this), new GPSDataLoader.OnLocationLoaded() {
            @Override
            public void onLocationLoadFinished(double lat, double lng) {
                Log.d("LOAD LOCATION", "LAT" + String.valueOf(lat) + " - LNG: " + String.valueOf(lng));
                locationTextView.setText("LAT" + String.valueOf(lat) + " - LNG: " + String.valueOf(lng));
            }
        }).loadLastKnownLocation();
    }
}
