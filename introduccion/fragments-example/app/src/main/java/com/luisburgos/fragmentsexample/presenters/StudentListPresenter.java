package com.luisburgos.fragmentsexample.presenters;

import android.app.ListFragment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.luisburgos.fragmentsexample.data.Student;
import com.luisburgos.fragmentsexample.data.StudentDataSource;

import java.util.List;

/**
 * Created by luisburgos on 8/02/16.
 */
public class StudentListPresenter implements StudentListContract.UserActionsListener {

    private StudentListContract.View mStudentListView;
    private StudentDataSource mDataSource;

    public StudentListPresenter(@NonNull StudentListContract.View studentListView, StudentDataSource dataSource) {
        mStudentListView = studentListView;
        mDataSource = dataSource;
    }

    @Override
    public void loadStudents() {
        List<Student> students = mDataSource.getAll();
        mStudentListView.showStudents(students);
    }

    @Override
    public void openStudentDetails(@NonNull Student requestedStudent) {
        mStudentListView.showStudentDetailUi(requestedStudent);
    }
}
