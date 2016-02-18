package com.luisburgos.studentsapp.studentdetail;

import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.luisburgos.studentsapp.data.students.StudentDataSource;
import com.luisburgos.studentsapp.domain.Student;
import com.luisburgos.studentsapp.utils.StudentValidator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 2/02/16.
 */
public class StudentDetailPresenter implements StudentDetailContract.UserActionsListener{

    private StudentDataSource mStudentsDataSource;
    private StudentDetailContract.View mStudentDetailView;

    private StudentValidator mValidator;

    public StudentDetailPresenter(
            @NonNull StudentDataSource studentDataSource,
            @NonNull StudentDetailContract.View studentDetailView) {
        mStudentDetailView = checkNotNull(studentDetailView, "studentDetailView cannot be null!");
        mStudentsDataSource = checkNotNull(studentDataSource, "studentsRepository cannot be null!");
        mValidator = new StudentValidator();
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
    public void saveStudentChanges(String id, String name, String lastName, String bachelorsDegree) {
        Student newStudent = new Student(id, name, lastName, bachelorsDegree);
        //TODO: Check validations when one value comes empty.
        if(mValidator.isEmpty(newStudent)){
            mStudentDetailView.showEmptyStudentError();
        }else {
            if(isValidStudentData(newStudent)){
                try {
                    mStudentsDataSource.open();
                    mStudentsDataSource.updateStudent(newStudent);
                    mStudentsDataSource.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mStudentDetailView.showStudentsList();
            }
            else {
                mStudentDetailView.showEmptyStudentError();
            }

        }
    }

    @Override
    public void editStudent() {
        mStudentDetailView.enableInformationEdition(true);
    }

    private boolean isValidStudentData(Student newStudent) {

        boolean isValid = true;

        if (!mValidator.validateName(newStudent.getName())) {
            mStudentDetailView.setNameErrorMessage();
            isValid = false;
        }

        if (!mValidator.validateLastName(newStudent.getLastName())) {
            mStudentDetailView.setLastNameErrorMessage();
            isValid = false;
        }

        return isValid;
    }

    private void showStudent(Student student) {

        String enrollmentID = student.getEnrollmentID();
        String name = student.getName();
        String lastName = student.getLastName();
        String bachelorsDegree = student.getBachelorsDegree();

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

        if(bachelorsDegree != null && bachelorsDegree.isEmpty()){
            mStudentDetailView.hideBachelorsDegree();
        }else {
            mStudentDetailView.showBachelorsDegree(bachelorsDegree);
        }

        mStudentDetailView.enableInformationEdition(false);
    }
}
