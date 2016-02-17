package com.luisburgos.studentsapp.addstudent;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.utils.Injection;

public class AddStudentActivity extends AppCompatActivity implements AddStudentContract.View {

    private AddStudentContract.UserActionsListener mActionsListener;
    private EditText enrollmentID;
    private EditText name;
    private EditText lastName;
    private Spinner bachelorsDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_close);

        enrollmentID = (EditText) findViewById(R.id.student_enrollment_id);
        name = (EditText) findViewById(R.id.student_name);
        lastName = (EditText) findViewById(R.id.student_lastName);
        bachelorsDegree = (Spinner) findViewById(R.id.student_bachelorsDegree);

        mActionsListener = new AddStudentPresenter(Injection.provideStudentsDataSource(this), this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_student, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.action_confirm) {
            mActionsListener.saveStudent(
                    enrollmentID.getText().toString().trim(),
                    name.getText().toString().trim(),
                    lastName.getText().toString().trim(),
                    bachelorsDegree.getSelectedItem().toString().trim()
            );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEmptyStudentMessage() {
        Snackbar.make(name, getString(R.string.empty_student_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showStudentsList() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
