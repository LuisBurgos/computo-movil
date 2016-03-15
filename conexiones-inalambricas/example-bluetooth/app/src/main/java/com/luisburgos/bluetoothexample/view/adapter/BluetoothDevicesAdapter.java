package com.luisburgos.bluetoothexample.view.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.utils.ScreenUtils;
import com.luisburgos.bluetoothexample.view.fragments.PairedDevicesFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisburgos on 2/03/16.
 */
public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesViewHolder> {

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private ArrayList<BluetoothDevice> mDevices;
    private BluetoothItemListener mItemListener;

    public BluetoothDevicesAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevices, BluetoothItemListener itemListener) {
        mContext = context;
        mDevices = bluetoothDevices;
        mItemListener = itemListener;
    }

    @Override
    public BluetoothDevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bluetooth_device, parent, false);
        BluetoothDevicesViewHolder vh = new BluetoothDevicesViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(BluetoothDevicesViewHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        final BluetoothDevice device = mDevices.get(position);

        holder.deviceName.setText(device.getName());
        holder.deviceAdress.setText(device.getAddress());
        holder.deviceBondState.setText(String.valueOf(device.getBondState()));
        holder.deviceType.setText(String.valueOf(device.getBondState()));
        //device.getBluetoothClass();
        //device.getUuids();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.onDeviceClick(device);
            }
        });

    }

    public void replaceData(List<BluetoothDevice> devices) {
        setList(devices);
        notifyDataSetChanged();
    }

    public boolean hasItem(BluetoothDevice device){
        return mDevices.contains(device);
    }

    public void addItem(BluetoothDevice device) {
        mDevices.add(device);
        notifyItemInserted(mDevices.size());
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDevices.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDevices.size());
        notifyDataSetChanged();
    }

    private void setList(List<BluetoothDevice> students) {
        mDevices = (ArrayList<BluetoothDevice>) students;
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    public BluetoothDevice getItem(int position) {
        return mDevices.get(position);
    }

    private void runEnterAnimation(View view, int position) {
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(ScreenUtils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    public interface BluetoothItemListener {
        void onDeviceClick(BluetoothDevice deviceClicked);
    }

}
