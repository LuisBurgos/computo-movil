package com.luisburgos.bluetoothexample.presenters.contracts;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by luisburgos on 2/03/16.
 */
public interface BluetoothDevicesContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showNoDevicesFoundMessage();

        void showDevices(List<BluetoothDevice> devices);

        void openDeviceLinkUI(String deviceID);

        void setDeviceVisibility(boolean active);

    }

    interface UserActionsListener {

        void scanDevices();

        void turnOnBluetooth();

        void turnOffBluetooth();

        void changeDeviceVisibility(boolean visible);

        void openDeviceLinkDetails(@NonNull BluetoothDevice device);

    }

}
