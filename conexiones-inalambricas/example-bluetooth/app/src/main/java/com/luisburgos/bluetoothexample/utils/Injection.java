package com.luisburgos.bluetoothexample.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.luisburgos.bluetoothexample.domain.BluetoothBroadcastReceiver;
import com.luisburgos.bluetoothexample.domain.BluetoothWrapper;
import com.luisburgos.bluetoothexample.domain.DevicesRepository;

/**
 * Created by luisburgos on 14/03/16.
 */
public class Injection {

    public static DevicesRepository provideDevicesRepository(Context context) {
        return new DevicesRepository(context, provideBluetoothAdapter());
    }

    public static BluetoothWrapper provideBluetoothWrapper(){
        return new BluetoothWrapper(provideBluetoothAdapter());
    }

    private static BluetoothAdapter provideBluetoothAdapter(){
        return BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothBroadcastReceiver provideDevicesBroadcastReceiver() {
        return new BluetoothBroadcastReceiver();
    }
}
