package com.luisburgos.studentsdatabase.presenters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luisburgos.studentsdatabase.data.StudentDataSource;
import com.luisburgos.studentsdatabase.domain.Student;

import java.sql.SQLException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentDetailPresenter implements StudentDetailContract.UserActionsListener{

    private StudentDataSource mStudentsDataSource;

    private StudentDetailContract.View mStudentDetailView;

    public StudentDetailPresenter(
            @NonNull StudentDataSource studentDataSource,
            @NonNull StudentDetailContract.View studentDetailView) {
        mStudentDetailView = checkNotNull(studentDetailView, "studentDetailView cannot be null!");
        mStudentsDataSource = checkNotNull(studentDataSource, "studentsRepository cannot be null!");
    }

    @Override
    public void openStudent(@Nullable String id) {
        if (null == id || id.isEmpty()) {
            mStudentDetailView.showMissingStudent();
            return;
        }

        mStudentDetailView.setProgressIndicator(true);
        Student student = null;
        try {
            mStudentsDataSource.open();
            student = mStudentsDataSource.getStudent(id);
            mStudentsDataSource.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mStudentDetailView.setProgressIndicator(false);
        if (null == student) {
            //mStudentDetailView.showMissingStudent();
        } else {
            showStudent(student);
        }
    }

    @Override
    public void saveStudentChanges(String id, String name, String lastName) {
        Student newStudent = new Student(id, name, lastName);
        //TODO: Check validations when one value comes empty.
        if(newStudent.isEmpty()){
            mStudentDetailView.showEmptyStudentError();
        }else {
            try {
                mStudentsDataSource.open();
                mStudentsDataSource.updateStudent(newStudent);
                mStudentsDataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mStudentDetailView.showStudentsList();
        }
    }

    @Override
    public void editStudent() {
        mStudentDetailView.enableInformationEdition(true);
    }

    private void showStudent(Student student) {

        String enrollmentID = student.getEnrollmentID();
        String name = student.getName();
        String lastName = student.getLastName();

        if (enrollmentID != null && enrollmentID.isEmpty()) {
            mStudentDetailView.hideEnrollmentID();
        } else {
            mStudentDetailView.showEnrollmentID(enrollmentID);
        }

        if (name != null && name.isEmpty()) {
            //TODO: Check, cause if is empty the EditText turns gone from view
            mStudentDetailView.hideName();
        } else {
            mStudentDetailView.showName(name);
        }

        if (lastName != null && lastName.isEmpty()) {
            mStudentDetailView.hideLastName();
        } else {
            mStudentDetailView.showLastName(lastName);
        }

        mStudentDetailView.enableInformationEdition(false);
    }
}
