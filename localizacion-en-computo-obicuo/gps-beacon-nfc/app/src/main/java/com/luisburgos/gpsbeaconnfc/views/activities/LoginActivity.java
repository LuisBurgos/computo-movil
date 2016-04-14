package com.luisburgos.gpsbeaconnfc.views.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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
    //private TextInputLayout usernameWrapper;
    //private TextInputLayout passwordWrapper;
    private TextView locationTextView;
    private TextView distanceTextView;
    private ProgressBar progressBar;

    private LoginContract.UserActionsListener mActionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        //usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        //passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);
        //usernameWrapper.setHint(getString(R.string.lbl_username_hint));
        //passwordWrapper.setHint(getString(R.string.lbl_password_hint));

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                /*mActionsListener.doLogin(
                        usernameWrapper.getEditText().getText().toString().trim(),
                        passwordWrapper.getEditText().getText().toString().trim()
                );*/
                mActionsListener.doLogin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mActionsListener.subscribeForLocationChanges(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActionsListener.loadLocation();
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
            finish();
        } else {
            //showLoginFailedMessage(getString(R.string.error_failed_login));
        }
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        if(loading){
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
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
}
