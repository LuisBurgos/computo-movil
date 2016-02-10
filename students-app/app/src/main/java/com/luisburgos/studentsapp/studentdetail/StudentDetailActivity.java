package com.luisburgos.studentsapp.studentdetail;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.utils.Injection;

public class StudentDetailActivity extends AppCompatActivity implements StudentDetailContract.View {

    public static final String EXTRA_STUDENT_ID = "STUDENT_ID";

    private StudentDetailContract.UserActionsListener mActionsListener;
    private String studentID;

    TextView textViewId;
    TextView textViewName;
    TextView textViewBachelorsDegree;
    ImageView imageViewPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        mActionsListener = new StudentDetailPresenter(Injection.provideStudentsRepository(), this);

        //Set the requested note id
        studentID = getIntent().getStringExtra(EXTRA_STUDENT_ID);


        textViewId = (TextView ) findViewById(R.id.detailId);
        textViewName = (TextView) findViewById(R.id.detailName);
        textViewBachelorsDegree = (TextView) findViewById(R.id.detailBachelorsDegree);
        imageViewPhoto = (ImageView) findViewById(R.id.detailPhoto);

    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.openStudent(studentID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            textViewId.setText(getString(R.string.loading));
            textViewName.setText(getString(R.string.loading));
            textViewBachelorsDegree.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showID(String id) {
        textViewId.setVisibility(View.VISIBLE);
        textViewId.setText(id);
    }

    @Override
    public void showName(String name) {
        textViewName.setVisibility(View.VISIBLE);
        textViewName.setText(name);
    }


    @Override
    public void showBachelorsDegree(String bachelorsDegree) {
        textViewBachelorsDegree.setVisibility(View.VISIBLE);
        textViewBachelorsDegree.setText(bachelorsDegree);
    }

    @Override
    public void hideID() {
        textViewId.setVisibility(View.GONE);
    }

    @Override
    public void hideBachelorsDegree() {
        textViewBachelorsDegree.setVisibility(View.GONE);
    }

    @Override
    public void hideName() {
        textViewName.setVisibility(View.GONE);
    }


    @Override
    public void showImage(String imageUrl) {
        //Empty
    }

    @Override
    public void hideImage() {
        //Empty
    }


    @Override
    public void showMissingStudent() {
        textViewId.setText("");
        textViewName.setText("No data");
        textViewBachelorsDegree.setVisibility(View.GONE);
    }
}
