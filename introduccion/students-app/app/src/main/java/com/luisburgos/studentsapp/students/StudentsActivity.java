package com.luisburgos.studentsapp.students;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.addstudent.AddStudentActivity;
import com.luisburgos.studentsapp.domain.Student;
import com.luisburgos.studentsapp.studentdetail.StudentDetailActivity;
import com.luisburgos.studentsapp.utils.Injection;
import com.luisburgos.studentsapp.utils.UserLogoutDialog;
import com.luisburgos.studentsapp.view.adapters.StudentsSimpleAdapter;
import com.luisburgos.studentsapp.view.listeners.StudentItemListener;
import com.luisburgos.studentsapp.view.listeners.StudentItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity implements StudentsContract.View, StudentItemTouchHelperCallback.SwipeReactor {

    private static final int REQUEST_ADD_STUDENT = 1;
    private static final int REQUEST_UPDATE_STUDENT = 2;

    private StudentsContract.UserActionsListener mActionsListener;
    private StudentsSimpleAdapter mListAdapter;

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView mRecyclerView;

    private Paint paint = new Paint();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.students_coordinator_layout);

        mActionsListener = new StudentsPresenter(Injection.provideStudentsDataSource(this), this);

        mListAdapter = new StudentsSimpleAdapter(this, new ArrayList<Student>(0), new StudentItemListener() {
            @Override
            public void onStudentClick(Student clickedStudent) {
                    mActionsListener.openStudentDetails(clickedStudent);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.students_list);
        mRecyclerView.setAdapter(mListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Pull-to-refresh
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(this, R.color.colorPrimary),
                ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mActionsListener.loadStudents(true);
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActionsListener.addNewStudent();
            }
        });
        initSwipe();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadStudents(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_UPDATE_STUDENT == requestCode && Activity.RESULT_OK == resultCode) {
            showSuccessMessage(getString(R.string.successfully_update_student_message));
        }

        if (REQUEST_ADD_STUDENT == requestCode && Activity.RESULT_OK == resultCode) {
            showSuccessMessage(getString(R.string.successfully_add_student_message));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_logout){
            doLogout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doLogout() {
        new UserLogoutDialog(this).show();
    }

    @Override
    public void setProgressIndicator(final boolean active) {
        if (this == null) {
            return;
        }
        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void showStudents(List<Student> students) {
        if(students != null){
            mListAdapter.replaceData(students);
        }
    }

    @Override
    public void showAddStudent() {
        Intent intent = new Intent(this,AddStudentActivity.class);
        startActivityForResult(intent, REQUEST_ADD_STUDENT);
    }

    @Override
    public void showStudentDetailUi(String id) {
        Intent intent = new Intent(StudentsActivity.this, StudentDetailActivity.class);
        intent.putExtra(StudentDetailActivity.EXTRA_STUDENT_ENROLLMENT_ID, id);
        startActivityForResult(intent, REQUEST_UPDATE_STUDENT);
    }

    @Override
    public void onLeftSwiped(int position) {
        showDeleteDialog(position);
    }

    @Override
    public void onRightSwiped(int position) {
        mActionsListener.openStudentDetails(mListAdapter.getItem(position));
    }

    private void initSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new StudentItemTouchHelperCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    private void showDeleteDialog(final int currentStudentPosition) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.delete_student_confirm_message));
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mActionsListener.deleteStudent(mListAdapter.getItem(currentStudentPosition).getEnrollmentID());
                mListAdapter.removeItem(currentStudentPosition);
                showSuccessMessage(getString(R.string.successfully_delete_student_message));
            }
        });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                mListAdapter.notifyDataSetChanged();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showSuccessMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
        mActionsListener.loadStudents(true);
    }
}
