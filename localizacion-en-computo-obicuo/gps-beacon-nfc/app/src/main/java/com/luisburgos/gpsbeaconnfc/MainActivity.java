package com.luisburgos.gpsbeaconnfc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String TAG = "DISTANCE2POINTS";

    @Bind(R.id.locationTextView) TextView locationTextView;
    @Bind(R.id.distanceTextView) TextView distanceTextView;
    @Bind(R.id.latitudeEditText) EditText latitudeEditText;
    @Bind(R.id.longitudeEditText) EditText longitudeEditText;
    @Bind(R.id.btnCalculate) Button btnCalculateDistance;

    private MainPresenter mActionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mActionsListener = new MainPresenter(this, new LocationPreferencesManager(this));
        btnCalculateDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitudeEditText.setError(null);
                longitudeEditText.setError(null);
                mActionsListener.calculateDistance();
            }
        });
        btnCalculateDistance.setEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActionsListener.subscribeForLocationChanges(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionsListener.loadCurrentLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActionsListener.unsubscribeForLocationChanges(this);
    }

    public void updateLocationView(String newLocationString){
        locationTextView.setText(newLocationString);
    }

    @Override
    public void setCurrentLocation(String location) {
        locationTextView.setText(location);
    }

    @Override
    public void setDistanceBetweenTwoLocations(String distanceBetweenTwoLocations) {
        distanceTextView.setText(distanceBetweenTwoLocations);
    }

    @Override
    public void showLocationSubscribeError() {
        Toast.makeText(this, "Ha ocurrido un error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public String getInputLatitude() {
        return latitudeEditText.getText().toString().trim();
    }

    @Override
    public String getInputLongitude() {
        return longitudeEditText.getText().toString().trim();
    }

    @Override
    public void setLongitudeErrorMessage() {
        longitudeEditText.setError("Ingresa una longitud válida");
    }

    @Override
    public void setLatitudeErrorMessage() {
        latitudeEditText.setError("Ingresa una latitud válida");
    }

    @Override
    public void enableDistanceCalculation() {
        btnCalculateDistance.setEnabled(true);
    }

}
