package com.luisburgos.studentsapp.addstudent;

import android.database.SQLException;
import android.support.annotation.NonNull;

import com.luisburgos.studentsapp.data.students.StudentDataSource;
import com.luisburgos.studentsapp.domain.Student;
import com.luisburgos.studentsapp.utils.StudentValidator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 3/02/16.
 */
public class AddStudentPresenter implements AddStudentContract.UserActionsListener {

    private AddStudentContract.View mAddStudentView;
    private StudentDataSource mStudentsDataSource;
    private final StudentValidator mValidator;

    public AddStudentPresenter(
            @NonNull StudentDataSource studentDataSource,
            @NonNull AddStudentContract.View addStudentView) {
        mAddStudentView = checkNotNull(addStudentView, "addStudentView cannot be null!");
        mStudentsDataSource = checkNotNull(studentDataSource, "studentsDataSource cannot be null!");
        mValidator = new StudentValidator();
    }

    @Override
    public void saveStudent(String id, String name, String lastName, String bachelorsDegree) {
        mAddStudentView.setProgressIndicator(true);
        Student newStudent = new Student(id, name, lastName, bachelorsDegree);

        if(mValidator.isEmpty(newStudent)){
            mAddStudentView.showEmptyStudentMessage();
        }else {
            if(isValidStudentData(newStudent)){
                try {
                    mStudentsDataSource.open();
                    //mStudentsDataSource.insertStudent(newStudent);
                    mStudentsDataSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mAddStudentView.showStudentsList();
            } else {
                mAddStudentView.setProgressIndicator(false);
            }
        }
    }

    private boolean isValidStudentData(Student newStudent) {

        boolean isValid = true;
        if (!mValidator.validateEnrollmentID(newStudent.getEnrollmentID())) {
            mAddStudentView.setEnrollmentIDErrorMessage();
            isValid = false;
        }

        if (!mValidator.validateName(newStudent.getName())) {
            mAddStudentView.setNameErrorMessage();
            isValid = false;
        }

        if (!mValidator.validateLastName(newStudent.getLastName())) {
            mAddStudentView.setLastNameErrorMessage();
            isValid = false;
        }

        if (!mValidator.validateBachelorsDegree(newStudent.getBachelorsDegree())) {
            mAddStudentView.setBachelorsDegreeErrorMessage();
            isValid = false;
        }
        return isValid;
    }
}
