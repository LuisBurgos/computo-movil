package com.luisburgos.studentsapp.students;

import android.database.SQLException;
import android.support.annotation.NonNull;
import android.util.Log;

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
            @NonNull StudentDataSource studentDataSource,
            @NonNull StudentsContract.View studentsView) {
        mStudentsView = checkNotNull(studentsView, "studentsView cannot be null!");
        mStudentsDataSource = checkNotNull(studentDataSource, "studentDataSource cannot be null!");
    }

    /**
     * This method load a list of students on the view.
     * When forceUpdate value is false, this method retrieves from cache, otherwise
     * if user swipes the view for reload this method refresh the data on cache before retrieve
     * the information to be displayed on the view.
     * @param forceUpdate determines if cache need to be updated
     */
    @Override
    public void loadStudents(boolean forceUpdate) {
        mStudentsView.setProgressIndicator(true);

        //Load all from database into cache when user swipe screen
        if (forceUpdate) {
            mStudentsDataSource.refreshData();
        }

        List<Student> students = null;
        try {
            //Get all from cache
            students = Lists.newArrayList(mStudentsDataSource.getAllStudents());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mStudentsView.setProgressIndicator(false);
        mStudentsView.showStudents(students);
    }

    /**
     * Tells the View to open a new activity for add a new student
     */
    @Override
    public void addNewStudent() {
        mStudentsView.showAddStudent();
    }

    /**
     * Tells the View to open a new activity for visualize a specific
     * student information
     * @param requestedStudent student to be shown
     */
    @Override
    public void openStudentDetails(@NonNull Student requestedStudent) {
        checkNotNull(requestedStudent, "requestedNote cannot be null!");
        mStudentsView.showStudentDetailUi(requestedStudent.getEnrollmentID());
    }

    /**
     * Handles delete operation confirmed by the user
     * @param id student to be deleted
     */
    @Override
    public void deleteStudent(String id) {
        try {
            mStudentsDataSource.deleteStudent(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
