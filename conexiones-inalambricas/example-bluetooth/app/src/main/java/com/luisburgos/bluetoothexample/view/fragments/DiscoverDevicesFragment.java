package com.luisburgos.bluetoothexample.view.fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.domain.BluetoothBroadcastReceiver;
import com.luisburgos.bluetoothexample.presenters.DiscoverDevicesContract;
import com.luisburgos.bluetoothexample.presenters.DiscoverDevicesPresenter;
import com.luisburgos.bluetoothexample.presenters.PairedDevicesPresenter;
import com.luisburgos.bluetoothexample.utils.Injection;
import com.luisburgos.bluetoothexample.view.adapter.BluetoothDevicesAdapter;

import java.util.ArrayList;

/**
 * Created by luisburgos on 10/03/16.
 */
public class DiscoverDevicesFragment extends Fragment implements DiscoverDevicesContract.View {

    private Button mButtonDiscover;
    private RecyclerView mRecyclerView;
    private BluetoothDevicesAdapter mDevicesAdapter;
    private DiscoverDevicesContract.UserActionsListener mActionsListener;
    private boolean discovering;

    public DiscoverDevicesFragment() {
        // Requires empty public constructor
    }

    public static DiscoverDevicesFragment newInstance() {
        return new DiscoverDevicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevicesAdapter = new BluetoothDevicesAdapter(getActivity().getApplicationContext(),
                new ArrayList<BluetoothDevice>(0),
                new BluetoothDevicesAdapter.BluetoothItemListener() {
                    @Override
                    public void onDeviceClick(BluetoothDevice deviceClicked) {

                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(discovering){
            mActionsListener.dropReceiver();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.startDiscover();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mActionsListener = new DiscoverDevicesPresenter(
                this,
                Injection.provideBluetoothWrapper(),
                Injection.provideDevicesBroadcastReceiver()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_devices, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.devices_list);
        mRecyclerView.setAdapter(mDevicesAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mButtonDiscover = (Button) root.findViewById(R.id.btnDiscover);

        mButtonDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(discovering){
                    mActionsListener.stopDiscover();
                    mActionsListener.dropReceiver();
                }else {
                    mActionsListener.startDiscover();
                }
            }
        });
        return root;
    }

    @Override
    public void setProgress(boolean active) {
        discovering = active;
        int color;
        String message;

        if(active){
            color = R.color.colorRed;
            message = getString(R.string.cancel_discover);
        }else {
            color = R.color.colorGreen;
            message = getString(R.string.start_discover);
        }

        mButtonDiscover.setBackgroundColor(getResources().getColor(color));
        mButtonDiscover.setText(message);
    }

    @Override
    public void showDeviceFound(BluetoothDevice device) {
        Log.i("Bluetooth Example", "FOUND: " + device.getName());
        mDevicesAdapter.addItem(device);
    }

    @Override
    public void setupReceiver(BluetoothBroadcastReceiver mBroadcastReceiver, boolean register) {
        if(register){
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND); //Add Adapter.FINISHED
            getActivity().registerReceiver(mBroadcastReceiver, filter);
        }else {
            getActivity().unregisterReceiver(mBroadcastReceiver);
        }
    }
}
