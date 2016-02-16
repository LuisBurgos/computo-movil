package com.luisburgos.studentsapp.data;

import com.luisburgos.studentsapp.domain.Student;

import java.util.List;

/**
 * Created by luisburgos on 2/02/16.
 */
public interface StudentsServiceApi {

    interface StudentsServiceCallback<T> {

        void onLoaded(T students);
    }

    void getAllStudents(StudentsServiceCallback<List<Student>> callback);

    void getStudent(String id, StudentsServiceCallback<Student> callback);

    void saveStudent(Student student);

}
