package com.luisburgos.useaccelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "SENSOR";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private TextView xAxis;
    private TextView yAxis;
    private TextView zAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        xAxis = (TextView) findViewById(R.id.textViewAxisX);
        yAxis = (TextView) findViewById(R.id.textViewAxisY);
        zAxis = (TextView) findViewById(R.id.textViewAxisZ);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        xAxis.setText(String.valueOf(x));
        yAxis.setText(String.valueOf(y));
        zAxis.setText(String.valueOf(z));
    }
}
