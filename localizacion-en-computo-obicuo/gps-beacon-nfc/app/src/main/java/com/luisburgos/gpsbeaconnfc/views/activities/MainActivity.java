package com.luisburgos.gpsbeaconnfc.views.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.repackaged.retrofit_v1_9_0.retrofit.RestAdapter;
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

    public static final String TAG = "GPS-BEACON-NFC";
    public static final String CAN_DOWNLOAD_CONTENT = "CAN_DOWNLOAD_CONTENT";

    @Bind(R.id.mainCoordinator) CoordinatorLayout mCoordinator;
    @Bind(R.id.jsonContentTextView) TextView jsonContentTextView;
    @Bind(R.id.holderLocationTextView) TextView holderLocationTextView;
    @Bind(R.id.btnDownload) Button btnDownload;
    @Bind(R.id.toolbar) Toolbar toolbar;

    private BluetoothAdapter bluetoothAdapter;
    private BeaconManager beaconManager;
    private MainPresenter mActionsListener;
    private ProgressDialog mProgressDialog;
    private NfcAdapter mNfcAdapter;
    boolean isOnClassroom = false;
    private int ENABLE_BLUETOOTH = 0;
    private boolean isNFCSupported;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        setupProgressDialog();
        isOnClassroom = getIntent().getBooleanExtra(CAN_DOWNLOAD_CONTENT, false);
        mActionsListener = new MainPresenter(
                this, Injection.provideLocationPreferencesManager(this), Injection.provideMainInteractor()
        );
        holderLocationTextView.setText(
                Injection.provideLocationPreferencesManager(this).getLastKnowLocation()
        );

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            isNFCSupported = false;
            Snackbar.make(mCoordinator, "This device doesn't support NFC.", Snackbar.LENGTH_INDEFINITE).show();
            enableContentDownloadByButton();
        } else {
            isNFCSupported = true;
            if (!mNfcAdapter.isEnabled()) {
                Snackbar.make(mCoordinator, "NFC is disabled.", Snackbar.LENGTH_LONG).show();
            }

            if(Injection.provideContentPreferencesManager(this).isOpenViaNFC()){
                handleIntent(getIntent());
            }
        }

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.d(MainActivity.TAG, "Monitoreando Region");
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        63463, 21120));
                mProgressDialog.setTitle("Buscando Beacon");
                mProgressDialog.show();
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(MainActivity.TAG, "Entrando a Region");
                mProgressDialog.dismiss();
                isOnClassroom = true;
                Injection.provideContentPreferencesManager(getApplicationContext()).registerIsOnClassroom();
                btnDownload.setEnabled(isOnClassroom);
                jsonContentTextView.setText("Estás cerca del BEACON, acercate al NFC Tag para descargar el contenido.");
                showNotification(
                        "BIENVENIDO AL CC1",
                        "¿Listo para descargar la información de la aisgnatura?");
                //mActionsListener.downloadContent(getApplicationContext());
            }
            @Override
            public void onExitedRegion(Region region) {
                mProgressDialog.dismiss();
                Log.d(MainActivity.TAG, "Saliendo de Region");
                isOnClassroom = false;
                Injection.provideContentPreferencesManager(getApplicationContext()).unregisterIsOnClassroom();
                btnDownload.setEnabled(isOnClassroom);
                showNotification("SALIDA","Acabas de salir del CC1. Nos vemos pronto");
            }
        });


        btnDownload.setEnabled(false);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnClassroom){
                    mActionsListener.downloadContent(getApplicationContext());
                }
            }
        });



    }

    private void enableContentDownloadByButton() {
        btnDownload.setVisibility(View.VISIBLE);
    }

    private void handleIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Toast.makeText(MainActivity.this, "Abierto via NFC", Toast.LENGTH_SHORT).show();
            ContentPreferencesManager contentPreferencesManager = Injection.provideContentPreferencesManager(this);
            if(contentPreferencesManager.isOnCampus() && contentPreferencesManager.isOnClassroom()){
                mActionsListener.downloadContent(getApplicationContext());
            }

        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {


        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*ContentPreferencesManager contentPreferencesManager = Injection.provideContentPreferencesManager(this);
        if(contentPreferencesManager.isOnCampus() && contentPreferencesManager.isOnClassroom()){
            mActionsListener.downloadContent(getApplicationContext());
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNFCSupported){
            NFCUtils.setupForegroundDispatch(this, mNfcAdapter);
        }

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, ENABLE_BLUETOOTH);
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isNFCSupported){
            NFCUtils.stopForegroundDispatch(this, mNfcAdapter);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }


    @Override
    public void showContent(String data) {
        FileManagerHelper.writeToInternalFile(this, data);
        jsonContentTextView.setText(data);
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
    public void showErrorMessage() {
        Snackbar.make(mCoordinator, "Error downloading content", Snackbar.LENGTH_INDEFINITE)
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsListener.downloadContent(getApplicationContext());;
                    }
                })
                .show();

        jsonContentTextView.setText("Error downloading");
    }

    @Override
    public void setCurrentLocation(String location) {
        holderLocationTextView.setText(location);
    }

    @Override
    public void showNoLongerInCampusMessage() {
        jsonContentTextView.setText("Error validating");
        Snackbar.make(mCoordinator, "You are not longer in campus", Snackbar.LENGTH_INDEFINITE)
                .setAction("REINGRESAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Injection
                                .provideContentPreferencesManager(getApplicationContext())
                                .unregisterIsOnClassroom()
                                .unregisterIsOnCampus();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                })
                .show();
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Cargando datos");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }
}
