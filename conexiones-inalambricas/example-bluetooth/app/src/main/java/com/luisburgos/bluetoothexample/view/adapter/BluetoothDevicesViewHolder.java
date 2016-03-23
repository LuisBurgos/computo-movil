package com.luisburgos.bluetoothexample.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.luisburgos.bluetoothexample.R;

/**
 * Created by luisburgos on 2/03/16.
 */
public class BluetoothDevicesViewHolder extends RecyclerView.ViewHolder {

    TextView deviceName;
    TextView deviceAdress;
    TextView deviceBondState;
    TextView deviceType;

    public BluetoothDevicesViewHolder(View view) {
        super(view);
        deviceName = (TextView) view.findViewById(R.id.deviceName);
        deviceAdress = (TextView) view.findViewById(R.id.deviceAdress);
        deviceBondState = (TextView) view.findViewById(R.id.deviceBondState);
        deviceType = (TextView) view.findViewById(R.id.deviceType);
    }
}