package com.luisburgos.studentslogin;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.UserActionsListener mActionsListener;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionsListener = new MainPresenter(this);

        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.doLogout();
            }
        });
    }

    @Override
    public void onLogoutResult(Boolean result, int code) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void showConfirmationDialog() {

    }

    @Override
    public void setProgressIndicator(boolean active) {

    }
}
