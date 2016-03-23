package com.luisburgos.bluetoothexample.domain;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by luisburgos on 15/03/16.
 */
public class BluetoothBroadcastReceiver extends BroadcastReceiver {

    private onReceiveNewBluetoothDevice mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //Log.i("Bluetooth Example", "FOUND: " + device.getName());
            mListener.onDeviceFound(device);
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            mListener.onFinishDiscovery();
        }
    }

    public void setReceiverListener(onReceiveNewBluetoothDevice listener){
        if(listener != null){
            mListener = listener;
        }
    }

    public interface onReceiveNewBluetoothDevice {
        void onDeviceFound(BluetoothDevice device);
        void onFinishDiscovery();
    }
}
