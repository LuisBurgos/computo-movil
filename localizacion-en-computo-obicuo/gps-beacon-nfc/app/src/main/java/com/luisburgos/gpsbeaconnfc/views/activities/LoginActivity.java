package com.luisburgos.gpsbeaconnfc.views.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.luisburgos.gpsbeaconnfc.presenters.contracts.LoginContract;
import com.luisburgos.gpsbeaconnfc.presenters.LoginPresenter;
import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.util.Injection;


public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextView welcomeMessage;
    private Button btnLogin;
    private TextView locationTextView;
    private TextView distanceTextView;
    private ProgressBar progressBar;
    private ProgressDialog mProgressDialog;

    private LoginContract.UserActionsListener mActionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupProgressDialog();
        mActionsListener = new LoginPresenter(this,
                Injection.provideUserSessionManager(this), Injection.provideLocationPreferencesManager(this));

        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/lobster.otf");
        if(welcomeMessage != null){
            welcomeMessage.setTypeface(robotoBoldCondensedItalic);
        }
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        distanceTextView = (TextView) findViewById(R.id.distanceTextView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                mActionsListener.doLogin(getApplicationContext());
            }
        });
        //mActionsListener.subscribeForLocationChanges(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        mActionsListener.loadLocation(getApplicationContext());
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActionsListener.unsubscribeForLocationChanges(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActionsListener.unsubscribeForLocationChanges(this);
    }

    @Override
    public void onLoginResult(Boolean result) {
        if(result){
            sendTo(MainActivity.class);
            this.finish();
        } else {
            //showLoginFailedMessage(getString(R.string.error_failed_login));
        }
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if(active) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setCanLoginState(boolean canLoginState) {
        btnLogin.setEnabled(canLoginState);
        if(canLoginState){
            distanceTextView.setTextColor(R.color.colorGreen);
        } else {
            distanceTextView.setTextColor(R.color.colorRed);
        }
    }

    @Override
    public void setCurrentLocation(String location) {
        locationTextView.setText(location);
    }

    @Override
    public void setCurrentDistance(String distance) {
        distanceTextView.setText(distance);
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
    public void showMain() {
        sendTo(MainActivity.class);
    }

    private void sendTo(Class classTo) {
        Intent intent = new Intent(LoginActivity.this, classTo);
        startActivity(intent);
        finish();
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage("Obteniendo ubicación actual");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
    }
}
