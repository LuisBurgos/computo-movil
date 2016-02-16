package com.luisburgos.studentsapp.students;

import android.support.annotation.NonNull;

import com.luisburgos.studentsapp.domain.Student;

import java.util.List;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentsContract {

    interface View {

        void setProgressIndicator(boolean active);

        void showStudents(List<Student> students);

        void showAddStudent();

        void showStudentDetailUi(String noteId);

    }

    interface UserActionsListener {

        void loadStudents(boolean forceUpdate);

        void addNewStudent();

        void openStudentDetails(@NonNull Student requestedStudent);
    }

}
