package com.luisburgos.bluetoothexample.presenters;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.design.widget.Snackbar;

import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.domain.BluetoothWrapper;
import com.luisburgos.bluetoothexample.domain.DevicesRepository;

/**
 * Created by luisburgos on 15/03/16.
 */
public class MainPresenter implements MainContract.UserActionsListener {

    private MainContract.View mView;
    private BluetoothWrapper mBluetoothWrapper;

    public MainPresenter(MainContract.View view, BluetoothWrapper bluetoothWrapper) {
        mView = view;
        mBluetoothWrapper = bluetoothWrapper;
    }

    @Override
    public void turnOnBluetooth() {
        if(!mBluetoothWrapper.isBluetoothAlreadyEnable()){
            mView.showTurnOnBluetooth();
        } else {
            mView.showBluetoothAlreadyOnMessage();
        }
    }

    @Override
    public void turnOffBluetooth() {
        if(mBluetoothWrapper.turnOffBluetooth()){
            mView.showTurnOffBluetoothMessage();
        }
    }

    @Override
    public void makeDeviceVisible() {
        mView.showMakeVisible();
    }

}
