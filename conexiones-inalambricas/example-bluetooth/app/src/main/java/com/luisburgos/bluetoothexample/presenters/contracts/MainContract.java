package com.luisburgos.bluetoothexample.presenters.contracts;

/**
 * Created by luisburgos on 15/03/16.
 */
public interface MainContract {

    interface View {

        void setVisibility(boolean visible);

        void setBluetoothActive(boolean active);

        void showMakeVisible();

        void showTurnOnBluetooth();

        void showBluetoothAlreadyOnMessage();

        void showTurnOffBluetoothMessage();
    }

    interface UserActionsListener {

        void turnOnBluetooth();

        void turnOffBluetooth();

        void makeDeviceVisible();

    }

}
