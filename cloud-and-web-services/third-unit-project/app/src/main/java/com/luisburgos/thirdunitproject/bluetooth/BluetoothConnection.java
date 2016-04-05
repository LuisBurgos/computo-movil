package com.luisburgos.thirdunitproject.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.luisburgos.thirdunitproject.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnection {

	private final BluetoothSocket mSocket;
	private BluetoothAdapter mBluetoothAdapter;

	private InputStream mInput;
	private OutputStream mOutput;

	private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");

	public BluetoothConnection(BluetoothDevice server){
		BluetoothSocket socket =  null;
		try{
			socket = server.createRfcommSocketToServiceRecord(MY_UUID);
		}catch(IOException exception){
			exception.printStackTrace();
		}

		this.mSocket = socket;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public void connectDevice(){
		mBluetoothAdapter.cancelDiscovery();
		try{
			mSocket.connect();
			mOutput = mSocket.getOutputStream();
			mInput = mSocket.getInputStream();
		}catch(IOException connectException){
			try{
				mSocket.close();
			}catch(IOException ignored){}
		}

	}

	public void sendData(String data){
		byte[] buffer = data.getBytes(Charset.forName("UTF-8"));
		try {
			if(mOutput != null){
				mOutput.write(buffer);
			}else{
				Log.d(MainActivity.TAG, "Output Stream Null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
