package com.luisburgos.bluetoothexample.listdevices;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.luisburgos.bluetoothexample.permissions.PermissionsActivity;
import com.luisburgos.bluetoothexample.permissions.PermissionsChecker;
import com.luisburgos.bluetoothexample.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothDevicesActivity extends AppCompatActivity {

    public static final int REQUEST_BLUETOOTH_ON = 1;

    Button turnOnButton, visibilityButton, listButton, turnOffButton, discoverButton;
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView listView;

    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    private static final int ON_REQUEST_BLUETOOTH_PERMISSIONS = 0;
    private CoordinatorLayout mCoordinator;
    private PermissionsChecker checker;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        checker = new PermissionsChecker(this);
        /**
         * If the app has the permission, the method returns PackageManager.PERMISSION_GRANTED, and
         * the app can proceed with the operation. If the app does not have the permission, the
         * method returns PERMISSION_DENIED, and the app has to explicitly ask the user for permission.
         */
        //askForPermissions();

        turnOnButton = (Button) findViewById(R.id.turnOnButton);
        visibilityButton =(Button)findViewById(R.id.visibilityButton);
        listButton =(Button)findViewById(R.id.listButton);
        turnOffButton =(Button)findViewById(R.id.turnOffButton);
        discoverButton =(Button)findViewById(R.id.discoverButton);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView = (ListView)findViewById(R.id.listView);

        ArrayList<String> list = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(mAdapter);
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checker.lacksPermissions(PERMISSIONS)) {
            //startPermissionsActivity();
            //Snackbar.make(mCoordinator, R.string.no_permissions, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void on(View v){
        if (!mBluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, REQUEST_BLUETOOTH_ON);
            Snackbar.make(mCoordinator, R.string.turn_on, Snackbar.LENGTH_INDEFINITE).show();
        } else {
            Snackbar.make(mCoordinator, R.string.already_on, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void off(View v){
        mBluetoothAdapter.disable();
        Snackbar.make(mCoordinator, R.string.turn_off, Snackbar.LENGTH_INDEFINITE).show();
    }

    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void list(View v){
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        ArrayList<String> list = new ArrayList<>();
        for(BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            Log.d("BLUETOOTH APP", bt.getName());
        }

        mAdapter.clear();

        if (pairedDevices.size() > 0) {
            Snackbar.make(mCoordinator, R.string.showing_paired_devices, Snackbar.LENGTH_INDEFINITE).show();
            for (BluetoothDevice device : pairedDevices) {
                mAdapter.add(device.getName() + "\n" + device.getAddress() + "   PAIRED");
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void discover(View v){
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
        Snackbar.make(mCoordinator, R.string.discovering, Snackbar.LENGTH_INDEFINITE).show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mAdapter.add(device.getName() + "\n" + device.getAddress() + "   UNPAIRED");
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, ON_REQUEST_BLUETOOTH_PERMISSIONS, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ON_REQUEST_BLUETOOTH_PERMISSIONS && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
