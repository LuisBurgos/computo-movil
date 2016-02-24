package com.luisburgos.studentsapp.data.students;

import android.content.Context;

import com.google.common.collect.Lists;
import com.luisburgos.studentsapp.data.students.cache.StudentsCacheManager;
import com.luisburgos.studentsapp.data.students.database.StudentDatabaseManager;
import com.luisburgos.studentsapp.domain.Student;

import java.util.Iterator;

/**
 * Created by luisburgos on 4/02/16.
 */
public class StudentDataSource {

    private StudentsCacheManager cache;
    private StudentDatabaseManager database;

    public StudentDataSource(Context context){
        cache = new StudentsCacheManager(context);
        database = new StudentDatabaseManager(context);
    }

    public long insertStudent(String enrollmentID, String name, String lastName, String bachelorsDegree) {
        return database.insertStudent(enrollmentID, name, lastName, bachelorsDegree);
    }

    public long insertStudent(Student newStudent){
        return database.insertStudent(
                newStudent.getEnrollmentID(),
                newStudent.getName(),
                newStudent.getLastName(),
                newStudent.getBachelorsDegree()
        );
    }

    /**
     * Function oriented to recovery all Student's from cache
     * @return List of Students
     */
    public Iterator<Student> getAllStudents(){
        if(cache.isEmpty()){
            cache.put(
                    Lists.newArrayList(
                            //database.loadFirstStudents(StudentsCacheManager.DEFAULT_CACHE_ELEMENTS)
                            database.getAllStudents()
                    )
            );
        }
        return cache.getAll().iterator();
    }

    public Student getStudent(String id) {
        /*database.open();
        Student student = database.getStudentById(id);
        database.close();*/
        Student student = cache.getStudentById(id);
        return student;
    }

    public int updateStudent(Student student){
        database.open();
        int updateStudent = database.updateStudent(student);
        database.close();
        return updateStudent;
    }

    public void deleteStudent(String studentID) {
        database.open();
        database.deleteStudent(studentID);
        database.close();
    }

    public void refreshData() {
        cache.clear();
        cache.put(
                Lists.newArrayList(database.getAllStudents())
        );
    }
}
