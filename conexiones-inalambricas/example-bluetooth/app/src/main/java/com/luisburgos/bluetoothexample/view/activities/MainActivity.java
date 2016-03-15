package com.luisburgos.bluetoothexample.view.activities;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;

import com.luisburgos.bluetoothexample.permissions.PermissionsActivity;
import com.luisburgos.bluetoothexample.permissions.PermissionsChecker;
import com.luisburgos.bluetoothexample.R;
import com.luisburgos.bluetoothexample.presenters.contracts.MainContract;
import com.luisburgos.bluetoothexample.presenters.MainPresenter;
import com.luisburgos.bluetoothexample.utils.Injection;
import com.luisburgos.bluetoothexample.view.fragments.DiscoverDevicesFragment;
import com.luisburgos.bluetoothexample.view.fragments.PairedDevicesFragment;

public class MainActivity extends AppCompatActivity implements MainContract.View, CompoundButton.OnCheckedChangeListener {

    public static final int ON_REQUEST_BLUETOOTH_PERMISSIONS = 0;
    public static final int REQUEST_BLUETOOTH_ON = 1;
    public static final int REQUEST_BLUETOOTH_DISCOVERABLE = 2;
    public static final String EXTRA_DEVICE_ADDRESS = "EXTRA_DEVICE_ADDRESS";
    private static final String[] PERMISSIONS = new String[] {
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
    };
    public static final String TAG = "Bluetooth Example App";

    private MainContract.UserActionsListener mActionsListener;
    private PermissionsChecker checker;

    private CoordinatorLayout mCoordinator;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    SwitchCompat menuIteVisibility, menuItemBluetooth;

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
        setupDrawerContent(mNavigationView);

        if (null == savedInstanceState) {
            initFragment(PairedDevicesFragment.newInstance());
        }

        menuIteVisibility = (SwitchCompat) mNavigationView.getMenu().findItem(R.id.menu_item_visibility).getActionView();
        menuIteVisibility.setOnCheckedChangeListener(this);

        menuItemBluetooth = (SwitchCompat) mNavigationView.getMenu().findItem(R.id.menu_item_bluetooth).getActionView();
        menuItemBluetooth.setOnCheckedChangeListener(this);
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

        if(requestCode == REQUEST_BLUETOOTH_ON && resultCode == Activity.RESULT_OK){
            Snackbar.make(mCoordinator, R.string.turn_on, Snackbar.LENGTH_SHORT).show();
        }

        if(requestCode == REQUEST_BLUETOOTH_DISCOVERABLE){
            if(resultCode == Activity.RESULT_CANCELED){
                Snackbar.make(mCoordinator, getString(R.string.visible_permission_not_granted), Snackbar.LENGTH_SHORT).show();
                menuIteVisibility.setChecked(false);
            }else if (resultCode == 300){
                Snackbar.make(mCoordinator, getString(R.string.now_visible), Snackbar.LENGTH_SHORT).show();
            }
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
        Snackbar.make(mCoordinator, R.string.turn_on, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setBluetoothActive(boolean active) {

    }

    @Override
    public void showMakeVisible() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, REQUEST_BLUETOOTH_DISCOVERABLE);
    }

    @Override
    public void showTurnOnBluetooth() {
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, REQUEST_BLUETOOTH_ON);
    }

    @Override
    public void showBluetoothAlreadyOnMessage() {
        Snackbar.make(mCoordinator, R.string.already_on, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showTurnOffBluetoothMessage() {
        Snackbar.make(mCoordinator, R.string.turn_off, Snackbar.LENGTH_SHORT).show();
    }

    private void initFragment(Fragment notesFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.container, notesFragment);
        transaction.commit();
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, ON_REQUEST_BLUETOOTH_PERMISSIONS, PERMISSIONS);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.menu_item_visibility){
            if (isChecked) {
                mActionsListener.makeDeviceVisible();
            }
        }

        if(buttonView.getId() == R.id.menu_item_bluetooth){
            if (isChecked) {
                mActionsListener.turnOnBluetooth();
            } else {
                mActionsListener.turnOffBluetooth();
            }
        }
        mDrawerLayout.closeDrawers();
    }
}
