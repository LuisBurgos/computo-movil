package com.luisburgos.gpsbeaconnfc.views.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.RestAdapter;
import com.luisburgos.gpsbeaconnfc.GPSBeaconNFCApplication;
import com.luisburgos.gpsbeaconnfc.managers.ContentPreferencesManager;
import com.luisburgos.gpsbeaconnfc.presenters.contracts.MainContract;
import com.luisburgos.gpsbeaconnfc.presenters.MainPresenter;
import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.util.Injection;
import com.luisburgos.gpsbeaconnfc.util.NFCUtils;
import com.luisburgos.gpsbeaconnfc.util.NdefReaderTask;
import com.luisburgos.gpsbeaconnfc.util.files.FileManagerHelper;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    @Bind(R.id.locationTextView) TextView locationTextView;
    @Bind(R.id.distanceTextView) TextView distanceTextView;
    @Bind(R.id.main_description_message) TextView mainDescriptionMessage;
    @Bind(R.id.btnLogin) Button btnLogin;

    private MainPresenter mActionsListener;
    private ProgressDialog mProgressDialog;
    private NfcAdapter mNfcAdapter;
    private boolean isNFCSupported;
    private ContentPreferencesManager contentPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(Injection.provideContentPreferencesManager(this).isOpenViaNFC()){
            handleIntent(getIntent());
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            isNFCSupported = false;
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            isNFCSupported = true;
            if (!mNfcAdapter.isEnabled()) {
                //Snackbar.make(mCoordinator, "NFC is disabled.", Snackbar.LENGTH_LONG).show();
            }
        }

        contentPreferencesManager = Injection.provideContentPreferencesManager(this);
        setupProgressDialog();

        mActionsListener = new MainPresenter(
                this, Injection.provideLocationPreferencesManager(this), Injection.provideMainInteractor()
        );

        handleIntent(getIntent());
        /*String action = getIntent().getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Toast.makeText(this, "Open by NFC", Toast.LENGTH_SHORT).show();
            Log.d(GPSBeaconNFCApplication.TAG, "Open by NFC");
            contentPreferencesManager.registerIsOpenViaNFC();
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Toast.makeText(this, "Open by NFC", Toast.LENGTH_SHORT).show();
            Log.d(GPSBeaconNFCApplication.TAG, "Open by NFC");
            contentPreferencesManager.registerIsOpenViaNFC();
        }*/

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.doLogin();
            }
        });
    }

    private void handleIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            contentPreferencesManager.registerIsOpenViaNFC();
            Toast.makeText(MainActivity.this, "Abierto via NFC", Toast.LENGTH_SHORT).show();
            ContentPreferencesManager contentPreferencesManager = Injection.provideContentPreferencesManager(this);
            if(contentPreferencesManager.isOnCampus() && contentPreferencesManager.isOnClassroom()){
                startActivity(new Intent(this, ContentActivity.class));
            }

        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) { //Do nothing now
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNFCSupported){
            NFCUtils.setupForegroundDispatch(this, mNfcAdapter);
        }

        if(Injection.provideContentPreferencesManager(getApplicationContext()).isOnClassroom()){
            mainDescriptionMessage.setText(getString(R.string.nfc_message));
            setupContentIntent();
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(true);
            mainDescriptionMessage.setText(getString(R.string.can_login_message));
        }

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
        mActionsListener.subscribeForLocationChanges(this);
    }

    private void setupContentIntent() {
        if( mNfcAdapter != null ){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, 0);
            IntentFilter[] intentFilter = new IntentFilter[] {};

            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter,
                    null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isNFCSupported){
            NFCUtils.stopForegroundDispatch(this, mNfcAdapter);
            contentPreferencesManager.unregisterIsOpenViaNFC();
        }
        mActionsListener.unsubscribeForLocationChanges(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActionsListener.unsubscribeForLocationChanges(this);
        Injection.provideContentPreferencesManager(getApplicationContext()).unregisterIsUserLogin();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(GPSBeaconNFCApplication.TAG, "Nuevo intent para NFC");
        handleIntent(intent);
    }

    @Override
    public void showContent(String data) {
        FileManagerHelper.writeToInternalFile(this, data);
        //jsonContentTextView.setText(data);
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if(active) {
            mProgressDialog.show();
        } else {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setCanLoginState(boolean canLoginState) {

        if(!Injection.provideContentPreferencesManager(this).isUserLoggedIn()){
            if(canLoginState){
                btnLogin.setVisibility(View.VISIBLE);
                btnLogin.setEnabled(canLoginState);
                mainDescriptionMessage.setText(getString(R.string.can_login_message));
            } else {
                mainDescriptionMessage.setText(getString(R.string.lbl_login_message));
            }
        }

    }

    @Override
    public void setCurrentLocation(String location) {
        locationTextView.setText(location);
    }

    @Override
    public void setCurrentDistance(String distance) {
        distanceTextView.setText(distance);
    }

    @Override
    public void showLocationSubscribeError() {
        showErrorMessage("Error al subscribirse a cambios de ubicación");
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginResult(boolean result) {

        if(Injection.provideContentPreferencesManager(getApplicationContext()).isOnClassroom()){
            mainDescriptionMessage.setText(getString(R.string.nfc_message));
        } else {
            mainDescriptionMessage.setText(getString(R.string.beacon_message));
        }
        Injection.provideContentPreferencesManager(this).registerIsUserLogin();
        btnLogin.setVisibility(View.GONE);
    }

    @Override
    public void showNoLongerInCampusMessage() {
        mainDescriptionMessage.setText(getString(R.string.lbl_login_message));
        Injection.provideContentPreferencesManager(getApplicationContext()).unregisterIsUserLogin();
        setCanLoginState(false);
        btnLogin.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Verificando ubicación");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
