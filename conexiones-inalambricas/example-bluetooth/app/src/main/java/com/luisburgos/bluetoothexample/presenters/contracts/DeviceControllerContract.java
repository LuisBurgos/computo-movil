package com.luisburgos.bluetoothexample.presenters.contracts;

import android.graphics.Bitmap;

/**
 * Created by luisburgos on 15/03/16.
 */
public interface DeviceControllerContract {

    interface View {

        void showPageUpMessage();

        void showPageDownMessage();

        void setDeviceAddress(String deviceAddress);

        void showDeviceConnectionError(String errorMessage);

        void requestTurnOnBluetooth();

        void setPresentationImage(Bitmap bmp);

        void showDeviceConnectionEstablish();
    }

    interface UserActionsListener {

        void pageUp();

        void pageDown();

        void startDeviceControl(String deviceAddress);

        void prepareDeviceConnection();
    }

}
