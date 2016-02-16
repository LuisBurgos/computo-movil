package com.luisburgos.studentslogin.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.luisburgos.studentslogin.presenters.MainContract;
import com.luisburgos.studentslogin.presenters.MainPresenter;
import com.luisburgos.studentslogin.R;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.UserActionsListener mActionsListener;

    private Button btnLogout;
    private TextView userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionsListener = new MainPresenter(this);

        userInfo = (TextView) findViewById(R.id.lbl_userinfo);

        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });

        mActionsListener.loadUserInformation();
    }

    @Override
    public void onLogoutResult(Boolean result, int code) {
        if(result){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    public void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("¿Seguro quieres cerrar sesión?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActionsListener.doLogout();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void showUsername(String username) {
        userInfo.setText(getString(R.string.lbl_username_prefix) + username);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        //setProgressBarVisibility(active);
    }
}
