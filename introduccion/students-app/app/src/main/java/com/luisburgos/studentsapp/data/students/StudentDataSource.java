package com.luisburgos.studentsapp.data.students;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.luisburgos.studentsapp.domain.Student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by luisburgos on 4/02/16.
 */
public class StudentDataSource {

    private SQLiteDatabase database;
    private StudentDBHelper studentDBHelper;
    private String[] allColumns = {
            StudentsDBContract.COLUMN_NAME_ENROLLMENT_ID,
            StudentsDBContract.COLUMN_NAME_NAME,
            StudentsDBContract.COLUMN_NAME_LAST_NAME,
            StudentsDBContract.COLUMN_NAME_BACHELORS_DEGREE
    };

    public StudentDataSource(Context context){
        studentDBHelper = new StudentDBHelper(context);
    }

    public void open() throws SQLException {
        database = studentDBHelper.getWritableDatabase();
    }

    public void close(){
        studentDBHelper.close();
    }

    /**
     * Function oriented to insert a new Student into DataBase
     * @param enrollmentID
     * @param name
     * @param lastName
     * @return row ID of the newly Student inserted row, or -1
     */
    public long insertStudent(String enrollmentID, String name, String lastName, String bachelorsDegree){
        ContentValues values = new ContentValues();
        values.put(StudentsDBContract.COLUMN_NAME_ENROLLMENT_ID, enrollmentID);
        values.put(StudentsDBContract.COLUMN_NAME_NAME, name);
        values.put(StudentsDBContract.COLUMN_NAME_LAST_NAME, lastName);
        values.put(StudentsDBContract.COLUMN_NAME_BACHELORS_DEGREE, bachelorsDegree);
        long newRowId;
        newRowId = database.insert(StudentsDBContract.TABLE_NAME,null,values);
        return newRowId;
    }

    public long insertStudent(Student newStudent){
        return insertStudent(
                newStudent.getEnrollmentID(),
                newStudent.getName(),
                newStudent.getLastName(),
                newStudent.getBachelorsDegree()
        );
    }

    /**
     * Function oriented to recovery all Student's rows from Database
     * @return List of Students from Database
     */
    public Iterator<Student> getAllStudents(){
        List<Student> students = new ArrayList<Student>();
        Student currentStudent;
        Cursor cursor = database.query(StudentsDBContract.TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            currentStudent = cursorToStudent(cursor);
            students.add(currentStudent);
            cursor.moveToNext();
        }
        cursor.close();
        return students.iterator();
    }

    public Student getStudent(String id) {

        Cursor cursor = database.query(
                StudentsDBContract.TABLE_NAME,
                allColumns,
                StudentsDBContract.COLUMN_NAME_ENROLLMENT_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        Student student = null;
        if (cursor != null){
            cursor.moveToFirst();
            student = cursorToStudent(cursor);
        }

        return student;
    }

    public int updateStudent(Student student){
        ContentValues values = new ContentValues();
        values.put(StudentsDBContract.COLUMN_NAME_NAME, student.getName());
        values.put(StudentsDBContract.COLUMN_NAME_LAST_NAME, student.getLastName());
        values.put(StudentsDBContract.COLUMN_NAME_BACHELORS_DEGREE, student.getBachelorsDegree());


        return database.update(StudentsDBContract.TABLE_NAME, values, StudentsDBContract.COLUMN_NAME_ENROLLMENT_ID + " =?",
                new String[]{String.valueOf(student.getEnrollmentID()) });
    }

    public void deleteStudent(Student student) {
        database.delete(
                StudentsDBContract.TABLE_NAME,
                StudentsDBContract.COLUMN_NAME_ENROLLMENT_ID + " = ?",
                new String[]{String.valueOf(student.getEnrollmentID())}
        );
        database.close();
    }

    public void deleteStudent(String studentID) {
        database.delete(
                StudentsDBContract.TABLE_NAME,
                StudentsDBContract.COLUMN_NAME_ENROLLMENT_ID + " = ?",
                new String[]{String.valueOf(studentID)}
        );
        database.close();
    }

    private Student cursorToStudent(Cursor cursor){
        Student student = new Student();
        student.setEnrollmentID(cursor.getString(0));
        student.setName(cursor.getString(1));
        student.setLastName(cursor.getString(2));
        student.setBachelorsDegree(cursor.getString(3));
        return student;
    }

}
