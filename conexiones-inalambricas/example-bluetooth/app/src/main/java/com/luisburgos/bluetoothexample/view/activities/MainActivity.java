package com.luisburgos.bluetoothexample.view.activities;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.luisburgos.bluetoothexample.permissions.PermissionsActivity;
import com.luisburgos.bluetoothexample.permissions.PermissionsChecker;
import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.presenters.MainContract;
import com.luisburgos.bluetoothexample.presenters.MainPresenter;
import com.luisburgos.bluetoothexample.utils.Injection;
import com.luisburgos.bluetoothexample.view.fragments.DiscoverDevicesFragment;
import com.luisburgos.bluetoothexample.view.fragments.PairedDevicesFragment;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final int REQUEST_BLUETOOTH_ON = 1;

    private MainContract.UserActionsListener mActionsListener;

    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };

    private static final int ON_REQUEST_BLUETOOTH_PERMISSIONS = 0;
    private CoordinatorLayout mCoordinator;
    private PermissionsChecker checker;

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_coordinator);
        checker = new PermissionsChecker(this);
        mActionsListener = new MainPresenter(this, Injection.provideBluetoothWrapper());

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                return false;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

        /**
         * If the app has the permission, the method returns PackageManager.PERMISSION_GRANTED, and
         * the app can proceed with the operation. If the app does not have the permission, the
         * method returns PERMISSION_DENIED, and the app has to explicitly ask the user for permission.
         */
        //askForPermissions();

        /*ArrayList<String> list = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(mAdapter);
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy*/

        setupDrawerContent(mNavigationView);

        if (null == savedInstanceState) {
            initFragment(PairedDevicesFragment.newInstance());
        }

        SwitchCompat item = (SwitchCompat) mNavigationView.getMenu().findItem(R.id.menu_item_visibility).getActionView();
        item.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mActionsListener.makeDeviceVisible();
                }
            }
        });

        SwitchCompat item4 = (SwitchCompat) mNavigationView.getMenu().findItem(R.id.menu_item_bluetooth).getActionView();
        item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mActionsListener.turnOnBluetooth();
                }else {
                    mActionsListener.turnOffBluetooth();
                }
            }
        });
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.menu_paired:
                fragmentClass = PairedDevicesFragment.class;
                break;
            case R.id.menu_discover:
                fragmentClass = DiscoverDevicesFragment.class;
                break;
            default:
                fragmentClass = PairedDevicesFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // menuItem.setChecked(true);
        if(menuItem.getItemId() != R.id.menu_item_visibility){
            setTitle(menuItem.getTitle());
        }
        mDrawerLayout.closeDrawers();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checker.lacksPermissions(PERMISSIONS)) {
            //startPermissionsActivity();
            //Snackbar.make(mCoordinator, R.string.no_permissions, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ON_REQUEST_BLUETOOTH_PERMISSIONS && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }

        if (requestCode == 0 );
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
        // s handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.home){
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setVisibility(boolean visible) {
        Snackbar.make(mCoordinator, R.string.turn_on, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void setBluetoothActive(boolean active) {

    }

    @Override
    public void showMakeVisible() {
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    @Override
    public void showTurnOnBluetooth() {
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, REQUEST_BLUETOOTH_ON);
        //Snackbar.make(mCoordinator, R.string.turn_on, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void showBluetoothAlreadyOnMessage() {
        Snackbar.make(mCoordinator, R.string.already_on, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void showTurnOffBluetoothMessage() {
        Snackbar.make(mCoordinator, R.string.turn_off, Snackbar.LENGTH_INDEFINITE).show();
    }

    private void initFragment(Fragment notesFragment) {
        // Add the NotesFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, notesFragment);
        transaction.commit();
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, ON_REQUEST_BLUETOOTH_PERMISSIONS, PERMISSIONS);
    }
}
