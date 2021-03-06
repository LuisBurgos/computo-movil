package com.luisburgos.studentsapp.studentdetail;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.utils.Injection;

public class StudentDetailActivity extends AppCompatActivity implements StudentDetailContract.View {

    public static final String EXTRA_STUDENT_ENROLLMENT_ID = "STUDENT_ENROLLMENT_ID";

    private StudentDetailContract.UserActionsListener mActionsListener;
    private String studentEnrollmentID;

    private TextInputLayout nameWrapper;
    private TextInputLayout lastNameWrapper;

    private EditText enrollmentID;
    private EditText name;
    private EditText lastName;
    private Spinner bachelorsDegree;
    private ArrayAdapter<CharSequence> mBachelorsDegreeAdapter;
    private Button btnEdit;
    private Button btnSaveEdition;
    private ImageView mImageView;
    private FloatingActionButton fab;
    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        //ab.setDisplayShowHomeEnabled(true);

        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getString(R.string.title_activity_student_details));
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));

        /*mImageView = (ImageView) findViewById(R.id.backdrop);
        Bitmap bitmap = ((BitmapDrawable) mImageView.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
                int primary = getResources().getColor(R.color.colorPrimary);
                collapsingToolbar.setContentScrimColor(palette.getMutedColor(primary));
                collapsingToolbar.setStatusBarScrimColor(palette.getDarkVibrantColor(primaryDark));
            }
        });*/
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.student_detail_coordinator_layout);


        mActionsListener = new StudentDetailPresenter(Injection.provideStudentsDataSource(this), this);

        studentEnrollmentID = getIntent().getStringExtra(EXTRA_STUDENT_ENROLLMENT_ID);

        enrollmentID = (EditText) findViewById(R.id.student_enrollment_id);
        name = (EditText) findViewById(R.id.student_name);
        lastName = (EditText) findViewById(R.id.student_lastName);
        nameWrapper = (TextInputLayout) findViewById(R.id.nameWrapper);
        lastNameWrapper = (TextInputLayout) findViewById(R.id.lastNameWrapper);

        bachelorsDegree = (Spinner) findViewById(R.id.student_bachelorsDegree);
        mBachelorsDegreeAdapter = ArrayAdapter.createFromResource(this,
                R.array.bachelors_degrees_array, android.R.layout.simple_spinner_item
        );
        mBachelorsDegreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bachelorsDegree.setAdapter(mBachelorsDegreeAdapter);

        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.editStudent();
            }
        });

        btnSaveEdition = (Button) findViewById(R.id.btn_save_edition);
        btnSaveEdition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.saveStudentChanges(
                        enrollmentID.getText().toString().trim(),
                        nameWrapper.getEditText().getText().toString().trim(),
                        lastNameWrapper.getEditText().getText().toString().trim(),
                        bachelorsDegree.getSelectedItem().toString().trim()
                );
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.photoFab);
        /*CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setAnchorId(View.NO_ID);
        fab.setLayoutParams(p);
        fab.setVisibility(View.GONE);*/
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mActionsListener.addNewStudent();
                Snackbar.make(mCoordinatorLayout, getString(R.string.action_take_photo),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_student_details, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_confirm) {
            Snackbar.make(mCoordinatorLayout, "Confirmar cambios",
                    Snackbar.LENGTH_SHORT).show();
        }

        if (id == R.id.action_edit) {
            Snackbar.make(mCoordinatorLayout, "Editar estudiante",
                    Snackbar.LENGTH_SHORT).show();
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            p.setBehavior(new FloatingActionButton.Behavior());
            p.setAnchorId(R.id.appbar);
            fab.setLayoutParams(p);
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.openStudent(studentEnrollmentID);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            enrollmentID.setText(getString(R.string.loading));
            name.setText(getString(R.string.loading));
            lastName.setText(getString(R.string.loading));
        }
    }

    @Override
    public void enableInformationEdition(boolean editionEnable) {
        enrollmentID.setEnabled(false);
        name.setEnabled(editionEnable);
        lastName.setEnabled(editionEnable);
        bachelorsDegree.setEnabled(editionEnable);
        btnSaveEdition.setEnabled(editionEnable);
    }

    @Override
    public void showEnrollmentID(String enrollmentID) {
        this.enrollmentID.setVisibility(View.VISIBLE);
        this.enrollmentID.setText(enrollmentID);
    }

    @Override
    public void showName(String name) {
        this.name.setVisibility(View.VISIBLE);
        this.name.setText(name);
    }


    @Override
    public void showLastName(String lastName) {
        this.lastName.setVisibility(View.VISIBLE);
        this.lastName.setText(lastName);
    }

    @Override
    public void hideEnrollmentID() {
        enrollmentID.setVisibility(View.GONE);
    }

    @Override
    public void hideLastName() {
        lastName.setVisibility(View.GONE);
    }

    @Override
    public void showStudentsList() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void hideName() {
        name.setVisibility(View.GONE);
    }

    @Override
    public void showMissingStudent() {
        enrollmentID.setText(getString(R.string.no_data));
        enrollmentID.setEnabled(false);
        name.setText(getString(R.string.no_data));
        name.setEnabled(false);
        lastName.setText(getString(R.string.no_data));
        lastName.setEnabled(false);
        bachelorsDegree.setEnabled(false);
    }

    @Override
    public void showEmptyStudentError() {
        Snackbar.make(name, getString(R.string.empty_student_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showBachelorsDegree(String bachelorsDegree) {
        this.bachelorsDegree.setVisibility(View.VISIBLE);
        this.bachelorsDegree.setSelection(mBachelorsDegreeAdapter.getPosition(bachelorsDegree));
    }

    @Override
    public void hideBachelorsDegree() {
        this.bachelorsDegree.setVisibility(View.GONE);
    }

    @Override
    public void setNameErrorMessage() {
        nameWrapper.setError(getString(R.string.error_name));
    }

    @Override
    public void setLastNameErrorMessage() {
        lastNameWrapper.setError(getString(R.string.error_last_name));
    }

}
