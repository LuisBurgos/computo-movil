package com.luisburgos.bluetoothexample.presenters;

import android.bluetooth.BluetoothDevice;

import com.luisburgos.bluetoothexample.domain.BluetoothBroadcastReceiver;

/**
 * Created by luisburgos on 15/03/16.
 */
public interface DiscoverDevicesContract {

    interface View {

        void setProgress(boolean active);

        void showDeviceFound(BluetoothDevice device);

        void setupReceiver(BluetoothBroadcastReceiver mBroadcastReceiver, boolean register);
    }

    interface UserActionsListener {

        void startDiscover();

        void stopDiscover();

        void dropReceiver();
    }


}
