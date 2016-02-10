package com.luisburgos.studentsapp.data;

import android.support.v4.util.ArrayMap;

import com.luisburgos.studentsapp.domain.Student;

import java.util.ArrayList;

/**
 * Created by luisburgos on 3/02/16.
 */
public class StudentsServiceApiEndpoint {

    static {
        DATA = new ArrayMap(6);
        addStudent("10004020", "Luis Burgos", "LIS");
        addStudent("10004021", "Luis Burgos", "LIS");
        addStudent("10004022", "Luis Burgos", "LIS");
        addStudent("10004023", "Luis Burgos", "LIS");
        addStudent("10004024", "Luis Burgos", "LIS");
        addStudent("10004025", "Luis Burgos", "LIS");
    }

    private final static ArrayMap<String, Student> DATA;

    private static void addStudent(String id, String name, String bachelorsDegree) {
        Student newStudent = new Student(id, name, bachelorsDegree);
        DATA.put(newStudent.getId(), newStudent);
    }

    /**
     * @return the Notes to show when starting the app.
     */
    public static ArrayMap<String, Student> loadPersistedNotes() {
        return DATA;
    }

}
