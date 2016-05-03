package com.luisburgos.gpsbeaconnfc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.luisburgos.gpsbeaconnfc.managers.ContentPreferencesManager;
import com.luisburgos.gpsbeaconnfc.util.Injection;
import com.luisburgos.gpsbeaconnfc.views.activities.MainActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by luisburgos on 29/04/16.
 */
public class GPSBeaconNFCApplication extends MultiDexApplication {

    public static final String TAG = "GPS-BEACON-NFC";
    public static final String CLASSROOM_BEACON_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
    public static final Integer BEACON_MAJOR = 63463;
    public static final Integer BEACON_MINOR = 21120;

    private BeaconManager beaconManager;
    private ContentPreferencesManager mContentPreferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mContentPreferencesManager = Injection.provideContentPreferencesManager(getApplicationContext());
        beaconManager = new BeaconManager(getApplicationContext());
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                Log.d(TAG, "Monitoreando Region");
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString(CLASSROOM_BEACON_UUID),
                        BEACON_MAJOR,
                        BEACON_MINOR)
                );
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.d(TAG, "Entrando a Region");
                mContentPreferencesManager.registerIsOnClassroom();
                showNotification("BIENVENIDO", "¿Listo para descargar la información de la aisgnatura?");
            }
            @Override
            public void onExitedRegion(Region region) {
                Log.d(TAG, "Saliendo de Region");
                mContentPreferencesManager.unregisterIsOnClassroom();
                showNotification("SALIDA","Acabas de salir del CC1. Nos vemos pronto");
            }
        });
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

}
