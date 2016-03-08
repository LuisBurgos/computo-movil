package com.luisburgos.bluetoothexample.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.luisburgos.bluetoothexample.R;

/**
 * Created by luisburgos on 2/03/16.
 */
public class BluetoothDevicesViewHolder extends RecyclerView.ViewHolder {

    TextView detailEnrollmentID;

    public BluetoothDevicesViewHolder(View view) {
        super(view);
        //detailEnrollmentID = (TextView) view.findViewById(R.id.detailEnrollmentID);
    }
}