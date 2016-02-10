package com.luisburgos.studentsapp.students;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.luisburgos.studentsapp.R;
import com.luisburgos.studentsapp.addstudent.AddStudentActivity;
import com.luisburgos.studentsapp.domain.Student;
import com.luisburgos.studentsapp.studentdetail.StudentDetailActivity;
import com.luisburgos.studentsapp.utils.Injection;
import com.luisburgos.studentsapp.view.adapters.StudentsAdapter;
import com.luisburgos.studentsapp.view.listeners.StudentItemListener;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity implements StudentsContract.View {

    private static final int REQUEST_ADD_STUDENT = 1;

    private StudentsContract.UserActionsListener mActionsListener;
    private StudentsAdapter mListAdapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mActionsListener = new StudentsPresenter(Injection.provideStudentsRepository(), this);

        mListAdapter = new StudentsAdapter(this, new ArrayList<Student>(0), new StudentItemListener() {
            @Override
            public void onStudentClick(Student clickedStudent) {
                    mActionsListener.openStudentDetails(clickedStudent);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.students_list);
        recyclerView.setAdapter(mListAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadStudents(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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
        mListAdapter.replaceData(students);
    }

    @Override
    public void showAddStudent() {
        Intent intent = new Intent(this,AddStudentActivity.class);
        startActivityForResult(intent, REQUEST_ADD_STUDENT);
    }

    @Override
    public void showStudentDetailUi(String id) {
        Intent intent = new Intent(StudentsActivity.this, StudentDetailActivity.class);
        intent.putExtra(StudentDetailActivity.EXTRA_STUDENT_ID, id);
        //intent.putExtra("name", student.getName());
        //intent.putExtra("degree", student.getBachelorsDegree());
        startActivity(intent);
    }

}
