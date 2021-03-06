package com.luisburgos.studentslogin.activities;

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
import android.widget.TextView;

import com.luisburgos.studentslogin.presenters.LoginContract;
import com.luisburgos.studentslogin.presenters.LoginPresenter;
import com.luisburgos.studentslogin.R;
import com.luisburgos.studentslogin.utils.Injection;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {

    private TextView welcomeMessage;
    private Button btnLogin;
    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;

    private LoginContract.UserActionsListener mActionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mActionsListener = new LoginPresenter(this, Injection.provideUsersDataSource(this));

        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        Typeface robotoBoldCondensedItalic = Typeface.createFromAsset(getAssets(), "fonts/lobster.otf");
        if(welcomeMessage != null){
            welcomeMessage.setTypeface(robotoBoldCondensedItalic);
        }
        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        btnLogin = (Button) findViewById(R.id.btn_login);

        usernameWrapper.setHint(getString(R.string.lbl_username_hint));
        passwordWrapper.setHint(getString(R.string.lbl_password_hint));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                mActionsListener.doLogin(
                        usernameWrapper.getEditText().getText().toString().trim(),
                        passwordWrapper.getEditText().getText().toString().trim()
                );
            }
        });
    }

    @Override
    public void onLoginResult(Boolean result, int code) {
        if(result){
            sendTo(MainActivity.class);
        } else {
            showLoginFailedMessage(getString(R.string.error_failed_login));
        }
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        usernameWrapper.setEnabled(!loading);
        passwordWrapper.setEnabled(!loading);
        btnLogin.setEnabled(!loading);
        if(loading){
            usernameWrapper.setError(null);
            passwordWrapper.setError(null);
            btnLogin.setText(getString(R.string.lbl_loading_message));
        }else {
            btnLogin.setText(getString(R.string.lbl_btn_login));
        }
    }

    @Override
    public void setUsernameErrorMessage() {
        usernameWrapper.setError(getString(R.string.error_invalid_username));
    }

    @Override
    public void setPasswordErrorMessage() {
        passwordWrapper.setError(getString(R.string.error_invalid_password));
    }
    
    @Override
    public void showEmptyDataMessage() {
        Snackbar.make(passwordWrapper, getString(R.string.empty_data_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoginFailedMessage(String message) {
        Snackbar.make(passwordWrapper, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showUserNonExistingMessage() {
        Snackbar.make(passwordWrapper, getString(R.string.error_non_existing_user), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showPasswordNotMatchMessage() {
        Snackbar.make(passwordWrapper, getString(R.string.error_password_not_match), Snackbar.LENGTH_LONG).show();
    }

    private void sendTo(Class classTo) {
        Intent intent = new Intent(LoginActivity.this, classTo);
        startActivity(intent);
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
