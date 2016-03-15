package com.luisburgos.bluetoothexample.presenters;

import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.luisburgos.bluetoothexample.domain.BluetoothWrapper;
import com.luisburgos.bluetoothexample.presenters.contracts.DeviceControllerContract;
import com.luisburgos.bluetoothexample.utils.BluetoothRemoteControlThread;
import com.luisburgos.bluetoothexample.view.activities.MainActivity;

/**
 * Created by luisburgos on 15/03/16.
 */
public class DeviceControllerPresenter implements DeviceControllerContract.UserActionsListener {

    private DeviceControllerContract.View mView;
    private BluetoothWrapper mBluetoothWrapper;
    private BluetoothDevice mRemoteDevice;
    private BluetoothRemoteControlThread mThread;

    public DeviceControllerPresenter(
            DeviceControllerContract.View view,
            BluetoothWrapper bluetoothWrapper
    ) {
        mView = view;
        mBluetoothWrapper = bluetoothWrapper;
    }

    @Override
    public void pageUp() {
        Log.d(MainActivity.TAG, "Page up");
        mThread.pgUp();
    }

    @Override
    public void pageDown() {
        Log.d(MainActivity.TAG, "Page down");
        mThread.pgDown();
    }

    @Override
    public void startDeviceControl(String deviceAddress) {
        mRemoteDevice = mBluetoothWrapper.getRemoteDevice(deviceAddress);

        if(mRemoteDevice == null) {
            mView.showDeviceConnectionError("Could not get remote device");
            return;
        }

        mView.setDeviceAddress(deviceAddress);
        mThread = new BluetoothRemoteControlThread((AppCompatActivity)mView, mRemoteDevice);
        if(mThread.connectDevice()){
            mView.showDeviceConnectionEstablish();
        } else {
            mView.showDeviceConnectionError("Could not establish remote connection");
        }

    }

    @Override
    public void prepareDeviceConnection() {
        if(!mBluetoothWrapper.isBluetoothAlreadyEnable()){
            mView.requestTurnOnBluetooth();
        }
    }
}
