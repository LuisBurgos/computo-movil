package com.luisburgos.gpsbeaconnfc.views.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
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
import com.luisburgos.gpsbeaconnfc.presenters.contracts.MainContract;
import com.luisburgos.gpsbeaconnfc.presenters.MainPresenter;
import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.managers.LocationPreferencesManager;
import com.luisburgos.gpsbeaconnfc.util.Injection;
import com.luisburgos.gpsbeaconnfc.util.files.FileManagerHelper;

import java.util.List;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    public static final String TAG = "GPS-BEACON-NFC";

    @Bind(R.id.mainCoordinator) CoordinatorLayout mCoordinator;
    @Bind(R.id.jsonContentTextView) TextView jsonContentTextView;
    @Bind(R.id.holderLocationTextView) TextView holderLocationTextView;
    @Bind(R.id.btnDownload) Button btnDownload;

    private BluetoothAdapter bluetoothAdapter;
    private BeaconManager beaconManager;
    private MainPresenter mActionsListener;
    private ProgressDialog mProgressDialog;
    boolean isOnClassroom = false;
    private int ENABLE_BLUETOOTH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mActionsListener = new MainPresenter(
                this, Injection.provideLocationPreferencesManager(this), Injection.provideMainInteractor()
        );
        holderLocationTextView.setText(
                Injection.provideLocationPreferencesManager(this).getLastKnowLocation()
        );

        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(MainActivity.TAG, "Entrando a Region");
                isOnClassroom = true;
                btnDownload.setEnabled(isOnClassroom);
                showNotification(
                        "BIENVENIDO AL CC1",
                        "¿Listo para descargar la información de la aisgnatura?");
            }
            @Override
            public void onExitedRegion(Region region) {
                Log.d(MainActivity.TAG, "Saliendo de Region");
                isOnClassroom = false;
                btnDownload.setEnabled(isOnClassroom);
                showNotification("SALIDA","Acabas de salir del CC1. Nos vemos pronto");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.d(MainActivity.TAG, "Monitoreando Region");
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        63463, 21120));
            }
        });

        btnDownload.setEnabled(false);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnClassroom){
                    mActionsListener.downloadContent();
                }
            }
        });
        setupProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter != null){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, ENABLE_BLUETOOTH);
        }
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
        Snackbar.make(mCoordinator, "", Snackbar.LENGTH_LONG)
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActionsListener.downloadContent();
                    }
                })
                .show();

        jsonContentTextView.setText("Error downloading");
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Cargando datos");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }
}
