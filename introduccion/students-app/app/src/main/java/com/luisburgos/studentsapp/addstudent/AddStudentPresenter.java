package com.luisburgos.studentsapp.addstudent;

import android.database.SQLException;
import android.support.annotation.NonNull;

import com.luisburgos.studentsapp.data.students.StudentDataSource;
import com.luisburgos.studentsapp.domain.Student;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by luisburgos on 3/02/16.
 */
public class AddStudentPresenter implements AddStudentContract.UserActionsListener {

    private AddStudentContract.View mAddStudentView;
    private StudentDataSource mStudentsDataSource;

    public AddStudentPresenter(
            @NonNull StudentDataSource studentDataSource,
            @NonNull AddStudentContract.View addStudentView) {
        mAddStudentView = checkNotNull(addStudentView, "addStudentView cannot be null!");
        mStudentsDataSource = checkNotNull(studentDataSource, "studentsDataSource cannot be null!");
    }

    @Override
    public void saveStudent(String id, String name, String lastName, String bachelorsDegree) {
        Student newStudent = new Student(id, name, lastName, bachelorsDegree);

        if(newStudent.isEmpty()){
            mAddStudentView.showEmptyStudentMessage();
        }else {
            try {
                mStudentsDataSource.open();
                //mStudentsDataSource.insertStudent(newStudent);
                mStudentsDataSource.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            mAddStudentView.showStudentsList();
        }
    }
}
