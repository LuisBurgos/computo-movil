package com.luisburgos.studentsapp.data.students.cache;

import android.content.Context;
import android.util.Log;

import com.luisburgos.studentsapp.domain.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by luisburgos on 18/02/16.
 */
public class StudentsCacheManager {

    private static final String STUDENTS_CACHE_FILE_NAME = "studentsCache.ser";
    public static final int DEFAULT_CACHE_ELEMENTS = 4;

    private Context mContext;
    private static File mStudentsCache;

    public StudentsCacheManager(Context context){
        mContext = context;
        mStudentsCache = new File(mContext.getCacheDir(), STUDENTS_CACHE_FILE_NAME);
    }

    public void put(final List<Student> studentList) {
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(mStudentsCache));
            oos.writeObject(studentList);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.i("STUDENTS APP", "Cache put: " + String.valueOf(studentList.toString()));
    }

    public List<Student> getAll() {
        List<Student> cachedStudents = null;

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(mStudentsCache));
            cachedStudents = (List<Student>) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return cachedStudents;
    }

    public boolean isEmpty(){
        Log.i("STUDENTS APP", "Cache size: " + String.valueOf(mStudentsCache.getTotalSpace()));
        return mStudentsCache.getTotalSpace() == 0;
    }


    public void clear() {
        File file = new File(mStudentsCache.getAbsolutePath());
        boolean deleted = file.delete();
        if(!deleted){
            Log.i("STUDENTS APP", "Cache size after delete: " + String.valueOf(mStudentsCache.getTotalSpace()));
        }else{
            Log.i("STUDENTS APP", "Cache file deleted");
        }

    }

    public Student getStudentById(String id) {
        Student student = null;
        for(Student current : getAll()){
            if(current.getEnrollmentID().equals(id)){
                student = current;
            }
        }
        return student;
    }
}
