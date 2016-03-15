package com.luisburgos.bluetoothexample.view.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.presenters.DeviceControllerPresenter;
import com.luisburgos.bluetoothexample.presenters.contracts.DeviceControllerContract;
import com.luisburgos.bluetoothexample.utils.Injection;

public class BluetoothDeviceControllerActivity extends AppCompatActivity implements DeviceControllerContract.View {

    private FloatingActionButton btnLeft;
    private FloatingActionButton btnRight;
    private CoordinatorLayout mCoordinator;
    private TextView mDeviceControllerAddress;
    private String mDeviceAddress;
    private ImageView mImageView;

    private DeviceControllerContract.UserActionsListener mActionsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_device_controller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDeviceAddress = getIntent().getExtras().getString(MainActivity.EXTRA_DEVICE_ADDRESS);

        mImageView = (ImageView) findViewById(R.id.deviceControllerImageView);
        mDeviceControllerAddress = (TextView)findViewById(R.id.deviceControllerAddress);
        mCoordinator = (CoordinatorLayout) findViewById(R.id.deviceControllerCoordinator);
        mActionsListener = new DeviceControllerPresenter(this,
                Injection.provideBluetoothWrapper());

        btnLeft = (FloatingActionButton) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsListener.pageDown();
            }
        });

        btnRight = (FloatingActionButton) findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsListener.pageUp();
            }
        });

        mActionsListener.prepareDeviceConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mActionsListener.prepareDeviceConnection();
        mActionsListener.startDeviceControl(mDeviceAddress);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MainActivity.REQUEST_BLUETOOTH_ON:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(mCoordinator, getString(R.string.turn_on), Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void showPageUpMessage() {
        Snackbar.make(mCoordinator, "Page Up", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showPageDownMessage() {
        Snackbar.make(mCoordinator, "Page Down", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setDeviceAddress(String deviceAddress) {
        mDeviceControllerAddress.setText(deviceAddress);
    }

    @Override
    public void showDeviceConnectionError(String errorMessage) {
        Snackbar.make(mCoordinator, errorMessage, Snackbar.LENGTH_INDEFINITE).show();
        btnLeft.setEnabled(false);
        btnRight.setEnabled(false);
    }

    @Override
    public void requestTurnOnBluetooth() {
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, MainActivity.REQUEST_BLUETOOTH_ON);
    }

    @Override
    public void setPresentationImage(Bitmap bmp) {
        mImageView.setImageBitmap(bmp);
    }

    @Override
    public void showDeviceConnectionEstablish() {
        Snackbar.make(mCoordinator, "Established remote device connection", Snackbar.LENGTH_SHORT).show();
    }
}
