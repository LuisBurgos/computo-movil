package com.luisburgos.accesswebservices;

import android.app.Activity;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewJSONContent;
    private TextView networkStatus;
    private Button btnMakeRequest;
    private Button btnClearContent;

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewJSONContent = (TextView) findViewById(R.id.textViewJSONContent);
        networkStatus = (TextView) findViewById(R.id.textViewNetworkStatus);
        btnClearContent = (Button) findViewById(R.id.btnClear);
        btnMakeRequest = (Button) findViewById(R.id.btnRequestJSON);

        btnMakeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            String JSONContent = null;
                            try {
                                JSONContent = requestJSON();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            final String finalJSONContent = JSONContent;
                            runOnUiThread(new Runnable(){

                                @Override
                                public void run() {
                                    if(finalJSONContent != null){
                                        textViewJSONContent.setText(finalJSONContent);
                                        Toast.makeText(MainActivity.this,
                                                finalJSONContent, Toast.LENGTH_LONG).show();
                                    }else {
                                        textViewJSONContent.setText("ERROR");
                                    }
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }
        });

        btnClearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewJSONContent.setText("");
            }
        });

    }

    private String requestJSON() throws IOException {
        Request request = new Request.Builder()
                .url("http://hmkcode.appspot.com/rest/controller/get.json")
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isConnected()){
            networkStatus.setText("Status: Connected");
            networkStatus.setBackgroundColor(Color.GREEN);
            btnMakeRequest.setEnabled(true);
        }
        else{
            networkStatus.setText("Status: Disconnected");
            networkStatus.setBackgroundColor(Color.RED);
            btnMakeRequest.setEnabled(false);
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }


}