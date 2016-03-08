package com.luisburgos.bluetoothexample.listdevices;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
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

    Button turnOnButton, visibilityButton, listButton, turnOffButton;
    private BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;
    ListView listView;

    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    private static final int ON_REQUEST_BLUETOOTH_PERMISSIONS = 0;
    private CoordinatorLayout mCoordinator;
    private PermissionsChecker checker;

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
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        listView = (ListView)findViewById(R.id.listView);
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
        if (!bluetoothAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, REQUEST_BLUETOOTH_ON);
            Snackbar.make(mCoordinator, R.string.turn_on, Snackbar.LENGTH_INDEFINITE).show();
        } else {
            Snackbar.make(mCoordinator, R.string.already_on, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    public void off(View v){
        bluetoothAdapter.disable();
        Snackbar.make(mCoordinator, R.string.turn_off, Snackbar.LENGTH_INDEFINITE).show();
    }

    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void list(View v){
        pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        for(BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
            Log.d("BLUETOOTH APP", bt.getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        if (pairedDevices.size() > 0) {
            Snackbar.make(mCoordinator, R.string.showing_paired_devices, Snackbar.LENGTH_INDEFINITE).show();
            for (BluetoothDevice device : pairedDevices) {
                adapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
        listView.setAdapter(adapter);
    }


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
