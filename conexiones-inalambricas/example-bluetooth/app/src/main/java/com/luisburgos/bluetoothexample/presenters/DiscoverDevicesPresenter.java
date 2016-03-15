package com.luisburgos.bluetoothexample.presenters;

import android.bluetooth.BluetoothDevice;

import com.luisburgos.bluetoothexample.domain.BluetoothBroadcastReceiver;
import com.luisburgos.bluetoothexample.domain.BluetoothWrapper;
import com.luisburgos.bluetoothexample.presenters.contracts.DiscoverDevicesContract;

/**
 * Created by luisburgos on 15/03/16.
 */
public class DiscoverDevicesPresenter implements DiscoverDevicesContract.UserActionsListener,
        BluetoothBroadcastReceiver.onReceiveNewBluetoothDevice {

    private DiscoverDevicesContract.View mView;
    private BluetoothWrapper mBluetoothWrapper;
    private BluetoothBroadcastReceiver mBroadcastReceiver;

    public DiscoverDevicesPresenter(
            DiscoverDevicesContract.View view,
            BluetoothWrapper bluetoothWrapper,
            BluetoothBroadcastReceiver bluetoothBroadcastReceiver) {
        mView = view;
        mBluetoothWrapper = bluetoothWrapper;
        mBroadcastReceiver = bluetoothBroadcastReceiver;
        mBroadcastReceiver.setReceiverListener(this);
    }

    @Override
    public void startDiscover() {
        if(mBluetoothWrapper.isBluetoothAlreadyDiscovering()){
            stopDiscover();
        }
        mView.setupReceiver(mBroadcastReceiver, true);
        mBluetoothWrapper.beginDiscovering();
        mView.setProgress(true);
    }

    @Override
    public void stopDiscover() {
        mBluetoothWrapper.stopDiscovering();
        mView.setProgress(false);
    }

    @Override
    public void requestPairingToDevice() {

    }

    @Override
    public void dropReceiver() {
        if(mBroadcastReceiver != null){
            mView.setupReceiver(mBroadcastReceiver, false);
        }
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
        mView.showDeviceFound(device);
    }

    @Override
    public void onFinishDiscovery() {
        mView.setProgress(false);
        mView.setupReceiver(mBroadcastReceiver, false);
    }
}