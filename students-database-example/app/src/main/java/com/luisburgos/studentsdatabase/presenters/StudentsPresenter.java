package com.luisburgos.studentsdatabase.presenters;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.luisburgos.studentsdatabase.data.StudentDataSource;
import com.luisburgos.studentsdatabase.domain.Student;

import java.sql.SQLException;
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
        mStudentsView.showStudents(students);
    }

    @Override
    public void openStudentDetails(@NonNull Student requestedStudent) {
        checkNotNull(requestedStudent, "requestedNote cannot be null!");
        mStudentsView.showStudentDetailUi(requestedStudent.getEnrollmentID());
    }
}
