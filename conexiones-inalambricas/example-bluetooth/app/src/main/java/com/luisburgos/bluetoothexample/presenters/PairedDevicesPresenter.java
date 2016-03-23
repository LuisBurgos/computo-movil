package com.luisburgos.bluetoothexample.presenters;

import android.bluetooth.BluetoothDevice;
import com.luisburgos.bluetoothexample.domain.DevicesRepository;
import com.luisburgos.bluetoothexample.presenters.contracts.PairedDevicesContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisburgos on 14/03/16.
 */
public class PairedDevicesPresenter implements PairedDevicesContract.UserActionsListener {

    private PairedDevicesContract.View mView;
    private DevicesRepository mDevicesRepository;

    public PairedDevicesPresenter(PairedDevicesContract.View view, DevicesRepository devicesRepository) {
        mView = view;
        mDevicesRepository = devicesRepository;
    }

    @Override
    public void loadDevices() {
        mView.setProgressIndicator(true);
        List<BluetoothDevice> devices = new ArrayList<BluetoothDevice>(mDevicesRepository.getPairedDevices());
        mView.setProgressIndicator(false);
        mView.showPairedDevices(devices);
    }

    @Override
    public void openBluetoothDeviceController(BluetoothDevice device) {
        mView.showBluetoothDeviceControllerUI(device);
    }
}
