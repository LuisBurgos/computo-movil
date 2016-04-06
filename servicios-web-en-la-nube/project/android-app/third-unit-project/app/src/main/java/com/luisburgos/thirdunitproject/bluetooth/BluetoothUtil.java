package com.luisburgos.thirdunitproject.bluetooth;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by luisburgos on 5/04/16.
 */
public class BluetoothUtil {
    public static boolean isBluetoothSupported() {
        return BluetoothAdapter.getDefaultAdapter() != null;
    }
}
