package com.luisburgos.bluetoothexample.domain;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by luisburgos on 14/03/16.
 */
public class DevicesRepository {

    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    public DevicesRepository(Context context, BluetoothAdapter bluetoothAdapter) {
        mContext = context;
        mBluetoothAdapter = bluetoothAdapter;
        pairedDevices = mBluetoothAdapter.getBondedDevices();

    }

    public Set<BluetoothDevice> getPairedDevices() {
        return pairedDevices;
    }
}
