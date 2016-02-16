package com.luisburgos.studentsapp.data;

import android.support.annotation.NonNull;

import com.luisburgos.studentsapp.domain.Student;

import java.util.List;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentsRepository {

    interface LoadStudentsCallback {

        void onStudentsLoaded(List<Student> students);
    }

    interface GetStudentCallback {

        void onStudentLoaded(Student student);
    }

    void getStudents(@NonNull LoadStudentsCallback callback);

    void getStudent(@NonNull String noteId, @NonNull GetStudentCallback callback);

    void saveStudent(@NonNull Student student);

    void refreshData();

}
