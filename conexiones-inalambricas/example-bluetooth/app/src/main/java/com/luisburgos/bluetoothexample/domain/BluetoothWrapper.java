package com.luisburgos.bluetoothexample.domain;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by luisburgos on 15/03/16.
 */
public class BluetoothWrapper {

    private BluetoothAdapter mBluetoothAdapter;

    public BluetoothWrapper(BluetoothAdapter bluetoothAdapter) {
        mBluetoothAdapter = bluetoothAdapter;
    }

    public boolean isBluetoothAlreadyEnable(){
        return mBluetoothAdapter.isEnabled();
    }

    public boolean isBluetoothAlreadyDiscovering(){
        return mBluetoothAdapter.isDiscovering();
    }

    public boolean turnOffBluetooth() {
        return mBluetoothAdapter.disable();
    }

    public void stopDiscovering() {
        mBluetoothAdapter.cancelDiscovery();
    }

    public void beginDiscovering() {
        mBluetoothAdapter.startDiscovery();
    }
}
