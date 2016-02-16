package com.luisburgos.studentsapp.data;

import android.os.Handler;
import android.support.v4.util.ArrayMap;

import com.luisburgos.studentsapp.domain.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisburgos on 3/02/16.
 */
public class StudentsServiceApiImpl implements StudentsServiceApi {

    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;
    private static final ArrayMap<String, Student> STUDENTS_SERVICE_DATA =
            StudentsServiceApiEndpoint.loadPersistedNotes();

    @Override
    public void getAllStudents(final StudentsServiceCallback callback) {
        // Simulate network by delaying the execution.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Student> students = new ArrayList<>(STUDENTS_SERVICE_DATA.values());
                callback.onLoaded(students);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void getStudent(final String id, final StudentsServiceCallback callback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Student student = STUDENTS_SERVICE_DATA.get(id);
                callback.onLoaded(student);
            }
        }, SERVICE_LATENCY_IN_MILLIS);
    }

    @Override
    public void saveStudent(Student student) {
        STUDENTS_SERVICE_DATA.put(student.getId(), student);
    }

}
