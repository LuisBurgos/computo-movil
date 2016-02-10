package com.luisburgos.fragmentsexample.presenters;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.luisburgos.fragmentsexample.data.Student;
import com.luisburgos.fragmentsexample.data.StudentDataSource;

/**
 * Created by luisburgos on 7/02/16.
 */
public class StudentDetailPresenter implements StudentDetailContract.UserActionsListener {

    private StudentDetailContract.View mStudentDetailView;
    private StudentDataSource mDataSource;

    public StudentDetailPresenter(StudentDetailContract.View studentDetailView, StudentDataSource dataSource) {
        mStudentDetailView = studentDetailView;
        mDataSource = dataSource;
    }

    @Override
    public void openStudent(@Nullable String id) {
        Student student = mDataSource.getById(id);

        if(student != null){
            showStudent(student);
        }else{
            mStudentDetailView.showMissingStudent();
        }
    }

    @Override
    public void saveStudentChanges(String id, String name, String lastName, String secondLast, String birthDate, String degree) {
        Student student = new Student(id, name, lastName, secondLast, birthDate, degree);
        boolean isStudentEmpty = student.isEmpty();
        if(isStudentEmpty){
            mStudentDetailView.showEmptyStudentError();
        }else{
            mDataSource.update(student);
            mStudentDetailView.showStudentsList();
            mStudentDetailView.loseFocus();
            mStudentDetailView.enableInformationEdition(false);
        }
    }

    @Override
    public void editStudent() {
        mStudentDetailView.enableInformationEdition(true);
    }

    private void showStudent(Student student) {

        String enrollmentID = student.getId();
        String name = student.getName();
        String lastName = student.getLastName();
        String secondLastName = student.getSecondLastName();
        String birthDate = student.getBirthDate();
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

        if (secondLastName != null && secondLastName.isEmpty()) {
            mStudentDetailView.hideSecondLastName();
        } else {
            mStudentDetailView.showSecondLastName(secondLastName);
        }

        if (birthDate != null && birthDate.isEmpty()) {
            mStudentDetailView.hideBirthDate();
        } else {
            mStudentDetailView.showBirthDate(birthDate);
        }

        if (bachelorsDegree != null && bachelorsDegree.isEmpty()) {
            mStudentDetailView.hideBachelorsDegree();
        } else {
            mStudentDetailView.showBachelorsDegree(bachelorsDegree);
        }

        mStudentDetailView.enableInformationEdition(false);
    }
}
