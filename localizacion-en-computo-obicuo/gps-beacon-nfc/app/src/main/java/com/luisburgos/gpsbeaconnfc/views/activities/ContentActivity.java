package com.luisburgos.gpsbeaconnfc.views.activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.luisburgos.gpsbeaconnfc.R;
import com.luisburgos.gpsbeaconnfc.network.callbacks.ArticlesCallback;
import com.luisburgos.gpsbeaconnfc.util.Injection;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentActivity extends AppCompatActivity implements ArticlesCallback {

    private ProgressDialog mProgressDialog;
    @Bind(R.id.jsonContentTextView) TextView jsonContentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        ButterKnife.bind(this);
        loadInformation();
    }

    private void loadInformation(){
        mProgressDialog.show();
        Injection.provideMainInteractor().loadArticlesData(this);
    }


    @Override
    public void onDataLoaded(String data) {
        mProgressDialog.dismiss();
        jsonContentTextView.setText(data);
    }

    @Override
    public void onFailure(String message) {
        mProgressDialog.dismiss();
        jsonContentTextView.setText("Error al cargar informacion");
    }

    private void setupProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Verificando ubicación");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
    }

}
