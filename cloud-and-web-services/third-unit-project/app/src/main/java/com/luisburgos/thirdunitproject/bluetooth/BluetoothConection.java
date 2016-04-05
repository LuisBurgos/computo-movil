package com.luisburgos.thirdunitproject.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConection {

	private final BluetoothSocket socket;
	private BluetoothAdapter mBluetoothAdapter;

	private InputStream in;
	private OutputStream out;

	private static final UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");

	public BluetoothConection(BluetoothDevice server){
		BluetoothSocket socket =  null;
		try{
			socket = server.createRfcommSocketToServiceRecord(MY_UUID);
		}catch(IOException exception){
			exception.printStackTrace();
		}

		this.socket = socket;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}

	public void connectDevice(){
		mBluetoothAdapter.cancelDiscovery();
		try{
			socket.connect();
			out = socket.getOutputStream();
			in = socket.getInputStream();
		}catch(IOException connectException){
			try{
				socket.close();
			}catch(IOException ignored){}
		}

	}

	public void sendData(String data){
		byte[] buffer = data.getBytes(Charset.forName("UTF-8"));
		try {
			if(out != null){
				out.write(buffer);
			}else{
				Log.d("BLUETOOTH DEVICE", "Output Stream Null");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
