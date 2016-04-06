package com.luisburgos.thirdunitproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.luisburgos.thirdunitproject.bluetooth.BluetoothConnection;
import com.luisburgos.thirdunitproject.bluetooth.BluetoothUtil;
import com.luisburgos.thirdunitproject.bluetooth.ListDeviceActivity;
import com.luisburgos.thirdunitproject.files.FileManagerHelper;
import com.luisburgos.thirdunitproject.files.JSONHelper;
import com.luisburgos.thirdunitproject.network.model.ArticlesCallback;;
import com.luisburgos.thirdunitproject.network.services.ServiceGenerator;
import com.luisburgos.thirdunitproject.network.model.ArticlesResponse;
import com.luisburgos.thirdunitproject.network.services.ArticlesApiService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity implements ArticlesCallback {

    public static final String TAG = "THIRD-UNIT";
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DEVICE_TO_CONNECT = 1;

    @Bind(R.id.mainCoordinator) CoordinatorLayout mCoordinator;
    @Bind(R.id.jsonContentTextView) TextView jsonContentTextView;
    @Bind(R.id.sendToDevice) Button sendToDevice;
    @Bind(R.id.chooseDevice) Button chooseDevice;

    private BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mProgressDialog;
    private BluetoothConnection mControlThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupProgressDialog();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(BluetoothUtil.isBluetoothSupported()){
            String contentFromServer = FileManagerHelper.readFileFromInternalStorage(this);
            if(contentFromServer != null){
                Log.d(TAG, "CONTENT PREVIOUS SAVED" + JSONHelper.prettyFormat(contentFromServer));
            }else{
                Log.d(TAG, "There is not CONTENT from Server store in File System");
            }

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            sendToDevice.setEnabled(false);
            chooseDevice.setEnabled(false);
            if(!mBluetoothAdapter.isEnabled()){
                requestBluetoothActivation();
            }
        }else{
            showBluetoothUnsupportedMessage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadArticlesData(this);
    }

    private void showBluetoothUnsupportedMessage(){
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.device_not_compatible))
                .setMessage(getString(R.string.device_not_compatible_explanation))
                .setPositiveButton(getString(R.string.device_not_compatible_action),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void loadArticlesData(final ArticlesCallback callback) {
        mProgressDialog.show();
        final ArticlesApiService articlesApiService = ServiceGenerator.createService(ArticlesApiService.class);
        Call<ArticlesResponse> call = articlesApiService.getArticles();
        Log.d(TAG, "ORIGINAL URL LOGIN REQ: " + call.request().toString());
        call.enqueue(new Callback<ArticlesResponse>() {

            @Override
            public void onResponse(Call<ArticlesResponse> call, Response<ArticlesResponse> response) {

                if (response.isSuccessful()) {
                    ArticlesResponse articlesResponse = response.body();

                    String JSONString = new Gson().toJson(articlesResponse);
                    JSONString = JSONHelper.prettyFormat(JSONString);

                    Log.d(TAG, "DATA LOADED: " + JSONString);
                    callback.onDataLoaded(JSONString);

                } else {
                    callback.onFailure("Error en la descarga");
                }
            }

            @Override
            public void onFailure(Call<ArticlesResponse> call, Throwable t) {
                callback.onFailure("Problema de Red");
            }
        });

    }

    @OnClick(R.id.sendToDevice)
    public void sendToDevice(){
        mControlThread.sendData(jsonContentTextView.getText().toString());
    }

    @OnClick(R.id.chooseDevice)
    public void chooseDevice(){
        if(mBluetoothAdapter.isEnabled()){
            Toast.makeText(this,"Buscando Dispositivos...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this ,ListDeviceActivity.class);
            startActivityForResult(intent,REQUEST_DEVICE_TO_CONNECT);
        }else {
            requestBluetoothActivation();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_DEVICE_TO_CONNECT:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras().getString(ListDeviceActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    mControlThread = new BluetoothConnection(device);
                    mControlThread.connectDevice();
                    sendToDevice.setEnabled(true);
                }
                break;
        }
    }

    private void requestBluetoothActivation() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("Cargando datos");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onDataLoaded(String data) {
        FileManagerHelper.writeToInternalFile(this, data);
        jsonContentTextView.setText(data);
        chooseDevice.setEnabled(true);
        mProgressDialog.dismiss();
    }

    @Override
    public void onFailure(String message) {
        Snackbar.make(mCoordinator, "", Snackbar.LENGTH_LONG)
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadArticlesData(MainActivity.this);
                    }
                })
                .show();

        jsonContentTextView.setText("Error downloading");
        mProgressDialog.dismiss();
    }
}