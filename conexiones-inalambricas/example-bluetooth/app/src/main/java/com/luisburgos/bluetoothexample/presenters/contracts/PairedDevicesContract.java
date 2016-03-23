package com.luisburgos.bluetoothexample.presenters.contracts;

import android.bluetooth.BluetoothDevice;

import java.util.List;

/**
 * Created by luisburgos on 14/03/16.
 */
public interface PairedDevicesContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showPairedDevices(List<BluetoothDevice> devices);

        void showBluetoothDeviceControllerUI(BluetoothDevice device);

    }

    interface UserActionsListener {

        void loadDevices();

        void openBluetoothDeviceController(BluetoothDevice device);

    }

}
