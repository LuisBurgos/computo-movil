package com.luisburgos.studentsapp.addstudent;

import android.app.Activity;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Spinner;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.utils.Injection;

public class AddStudentActivity extends AppCompatActivity implements AddStudentContract.View {

    private AddStudentContract.UserActionsListener mActionsListener;
    private TextInputLayout enrollmentIDWrapper;
    private TextInputLayout nameWrapper;
    private TextInputLayout lastNameWrapper;
    private CoordinatorLayout coordinatorLayout;
    private Spinner bachelorsDegree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_action_close);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.addstudent_coordinator_layout);
        bachelorsDegree = (Spinner) findViewById(R.id.student_bachelorsDegree);
        bachelorsDegree.setPrompt(getString(R.string.prompt_bachelors_degree));
        enrollmentIDWrapper = (TextInputLayout) findViewById(R.id.enrollmentIDWrapper);
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        lastNameWrapper = (TextInputLayout) findViewById(R.id.lastNameWrapper);

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
                    enrollmentIDWrapper.getEditText().getText().toString().trim(),
                    nameWrapper.getEditText().getText().toString().trim(),
                    lastNameWrapper.getEditText().getText().toString().trim(),
                    bachelorsDegree.getSelectedItem().toString().trim()
            );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        if(loading){
            enrollmentIDWrapper.setError(null);
            nameWrapper.setError(null);
            lastNameWrapper.setError(null);
        }
    }

    @Override
    public void showEmptyStudentMessage() {
        Snackbar.make(coordinatorLayout, getString(R.string.empty_student_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showStudentsList() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void setEnrollmentIDErrorMessage() {
        enrollmentIDWrapper.setError(getString(R.string.error_enrollment_id));
    }

    @Override
    public void setNameErrorMessage() {
        nameWrapper.setError(getString(R.string.error_name));
    }

    @Override
    public void setLastNameErrorMessage() {
        lastNameWrapper.setError(getString(R.string.error_last_name));
    }

    @Override
    public void setBachelorsDegreeErrorMessage() {
        //No showing
    }
}
