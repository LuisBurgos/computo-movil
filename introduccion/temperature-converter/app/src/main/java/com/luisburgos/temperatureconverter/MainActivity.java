package com.luisburgos.temperatureconverter;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private MainContract.UserActionsListener mListener;
    private Button buttonConvert;
    private EditText editTextDegrees;
    private EditText editTextResult;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListener = new MainPresenter(this);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroupDegrees);
        editTextDegrees = (EditText) findViewById(R.id.editTextDegrees);
        editTextResult = (EditText) findViewById(R.id.editTextResult);
        buttonConvert = (Button) findViewById(R.id.buttonConvert);
        buttonConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.doConversion();
            }
        });
    }

    @Override
    public void showMissingDegreeParameter() {
        Snackbar.make(editTextDegrees, getString(R.string.error_missing_degree_param), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public String getDegrees() {
        return editTextDegrees.getText().toString().trim();
    }

    @Override
    public int getTypeConversion() {
        return radioGroup.getCheckedRadioButtonId();
    }

    @Override
    public void setResult(String newValue) {
        editTextResult.setText(newValue);
    }

    @Override
    public void clearResult() {
        setResult("");
    }

}
