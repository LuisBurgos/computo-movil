package com.luisburgos.studentsapp.login;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.students.StudentsActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameWrapper = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper = (TextInputLayout) findViewById(R.id.passwordWrapper);

        btnLogin = (Button) findViewById(R.id.btn_login);

        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                if(validateDataLogin()){
                    sendTo(StudentsActivity.class);
                }
            }
        });
    }

    private boolean validateEmail(String email) {
        //matcher = pattern.matcher(email);
        //return matcher.matches();
        return true;
    }

    private boolean validatePassword(String password) {
        return password.length() > 5;
    }

    private boolean validateDataLogin() {
        String username = usernameWrapper.getEditText().getText().toString();
        String password = passwordWrapper.getEditText().getText().toString();

        if (!validateEmail(username)) {
            usernameWrapper.setError("Not a valid email address!");
            return false;
        } else if (!validatePassword(password)) {
            passwordWrapper.setError("Not a valid password!");
            return false;
        } else {
            usernameWrapper.setErrorEnabled(false);
            passwordWrapper.setErrorEnabled(false);
            return true;
        }
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
