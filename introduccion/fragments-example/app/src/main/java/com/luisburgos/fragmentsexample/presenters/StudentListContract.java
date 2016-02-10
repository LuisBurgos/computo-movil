package com.luisburgos.fragmentsexample.presenters;

import android.support.annotation.NonNull;

import com.luisburgos.fragmentsexample.data.Student;

import java.util.List;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentListContract {

    interface View {

        void showStudents(List<Student> students);

        void showStudentDetailUi(Student student);

        UserActionsListener getActionsListener();
    }

    interface UserActionsListener {

        void loadStudents();

        void openStudentDetails(@NonNull Student requestedStudent);
    }

}
