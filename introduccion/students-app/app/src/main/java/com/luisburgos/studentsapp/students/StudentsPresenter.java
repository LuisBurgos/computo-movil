package com.luisburgos.studentsapp.students;

import android.database.SQLException;
import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.luisburgos.studentsapp.data.students.StudentDataSource;
import com.luisburgos.studentsapp.domain.Student;


import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentsPresenter implements StudentsContract.UserActionsListener {

    private final StudentDataSource mStudentsDataSource;
    private final StudentsContract.View mStudentsView;

    public StudentsPresenter(
            @NonNull StudentDataSource studentsRepository,
            @NonNull StudentsContract.View studentsView) {
        mStudentsView = checkNotNull(studentsView, "studentsView cannot be null!");
        mStudentsDataSource = checkNotNull(studentsRepository, "studentsRepository cannot be null!");
    }

    @Override
    public void loadStudents(boolean forceUpdate) {
        mStudentsView.setProgressIndicator(true);
        if (forceUpdate) {
            //mStudentsDataSource.refreshData();
        }

        List<Student> students = null;
        try {
            mStudentsDataSource.open();
            students = Lists.newArrayList(mStudentsDataSource.getAllStudents());
            mStudentsDataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mStudentsView.setProgressIndicator(false);

        //boolean isListEmpty = students == null || students.isEmpty();
        mStudentsView.showStudents(students);
    }

    @Override
    public void addNewStudent() {
        mStudentsView.showAddStudent();
    }

    @Override
    public void openStudentDetails(@NonNull Student requestedStudent) {
        checkNotNull(requestedStudent, "requestedNote cannot be null!");
        mStudentsView.showStudentDetailUi(requestedStudent.getEnrollmentID());
    }
}
