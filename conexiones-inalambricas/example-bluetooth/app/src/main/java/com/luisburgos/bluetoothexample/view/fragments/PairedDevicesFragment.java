package com.luisburgos.bluetoothexample.view.fragments;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.presenters.contracts.PairedDevicesContract;
import com.luisburgos.bluetoothexample.presenters.PairedDevicesPresenter;
import com.luisburgos.bluetoothexample.utils.Injection;
import com.luisburgos.bluetoothexample.view.activities.BluetoothDeviceControllerActivity;
import com.luisburgos.bluetoothexample.view.activities.MainActivity;
import com.luisburgos.bluetoothexample.view.adapter.BluetoothDevicesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisburgos on 10/03/16.
 */
public class PairedDevicesFragment extends Fragment implements PairedDevicesContract.View {

    private RecyclerView mRecyclerView;
    private BluetoothDevicesAdapter mDevicesAdapter;
    private PairedDevicesContract.UserActionsListener mActionsListener;

    public PairedDevicesFragment() {

    }

    public static PairedDevicesFragment newInstance() {
        return new PairedDevicesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevicesAdapter = new BluetoothDevicesAdapter(getActivity().getApplicationContext(), new ArrayList<BluetoothDevice>(0),
                new BluetoothDevicesAdapter.BluetoothItemListener() {
            @Override
            public void onDeviceClick(BluetoothDevice deviceClicked) {
                mActionsListener.openBluetoothDeviceController(deviceClicked);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadDevices();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        mActionsListener = new PairedDevicesPresenter(this, Injection.provideDevicesRepository(getActivity().getApplicationContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_content_list, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.devices_list);
        mRecyclerView.setAdapter(mDevicesAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;
    }

    @Override
    public void setProgressIndicator(boolean active) {

    }

    @Override
    public void showPairedDevices(List<BluetoothDevice> devices) {
        if(devices != null){
            mDevicesAdapter.replaceData(devices);
        }
    }

    @Override
    public void showBluetoothDeviceControllerUI(BluetoothDevice device) {
        Intent remoteControlIntent = new Intent(getActivity(), BluetoothDeviceControllerActivity.class);
        remoteControlIntent.putExtra(MainActivity.EXTRA_DEVICE_ADDRESS, device.getAddress());
        startActivity(remoteControlIntent);
    }

}
