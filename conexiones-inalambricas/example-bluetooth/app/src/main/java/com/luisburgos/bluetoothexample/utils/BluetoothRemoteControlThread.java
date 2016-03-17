package com.luisburgos.bluetoothexample.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.luisburgos.bluetoothexample.view.activities.MainActivity;

public class BluetoothRemoteControlThread extends Thread{

    private final BluetoothSocket mSocket;
    private final BluetoothDevice mDevice;

    private BluetoothAdapter mBluetoothAdapter;

    private InputStream in;
    private OutputStream out;

    private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");

    public BluetoothRemoteControlThread(BluetoothDevice device){
        BluetoothSocket tmp =  null;
        mDevice = device;

        try{
            tmp = mDevice.createRfcommSocketToServiceRecord(MY_UUID);
        }catch(IOException exp){
            exp.printStackTrace();
        }

        mSocket = tmp;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void run(){
        mBluetoothAdapter.cancelDiscovery();

        try{
            Log.i(MainActivity.TAG, "Before socket connect");
            mSocket.connect();
            Log.i(MainActivity.TAG, "After socket connect");
            out = mSocket.getOutputStream();
            in = mSocket.getInputStream();
        }catch(IOException connectException){
            connectException.printStackTrace();
            try{
                mSocket.close();
            }catch(IOException exp){
                exp.printStackTrace();
            }
            return;
        }
    }

    public void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) { }
    }

    public void pgDown(){
        try {
            out.write(1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pgUp(){
        byte image[] = new byte[200];
        try {
            out.write(2);
			/*in.read(image);
			int l = image.length;
			int a1 = in.available();
			in.read(new byte[150]);
			int a2 = in.available();
			Bitmap bmp = BitmapFactory.decodeByteArray(image, 0, image.length);
			((DeviceControllerContract.View) act).setPresentationImage(bmp);*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
