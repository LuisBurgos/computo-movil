package com.luisburgos.fragmentsexample.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by luisburgos on 8/02/16.
 */
public class StudentDataSource {

    public static final String DEFAULT_FILE_NAME = "studentslist";
    private Context mContext;
    private List<Student> mCache;

    public StudentDataSource(@NonNull Context context) {
        mContext = context;
        initCache();
    }

    public void update(Student student){
        //String entryToUpdate = stringify(student);
        int index = getIndexOf(student.getId());
        mCache.set(index, student);
        StudentFileWriter.replaceContent(mContext, stringifyList(mCache));
    }

    private String stringify(Student student) {
        return student.getId() + "," +
                student.getName() + "," +
                student.getLastName() + "," +
                student.getSecondLastName() + "," +
                student.getBirthDate() + "," +
                student.getBachelorsDegree() + System.getProperty("line.separator");
    }

    private LinkedList<String> stringifyList(List<Student> students) {
        LinkedList stringifyList = new LinkedList();

        for(Student student : students){
            String line = student.getId() + "," +
                    student.getName() + "," +
                    student.getLastName() + "," +
                    student.getSecondLastName() + "," +
                    student.getBirthDate() + "," +
                    student.getBachelorsDegree() + System.getProperty("line.separator");
            stringifyList.add(line);
        }

        return stringifyList;
    }

    public List<Student> getAll(){
        if(mCache == null || mCache.isEmpty()){
            initCache();
        }

        if(mCache.isEmpty()){
            StudentFileWriter.initFileToInternalStorage(mContext);
            initCache();
        }

        return mCache;
    }

    public Student getById(String id){
        return mCache.get(getIndexOf(id));
    }

    private void initCache() {
        mCache = StudentFileReader.readFileFromInternalStorage(mContext);
    }

    private int getIndexOf(String id) {
        for(int i = 0 ; i< mCache.size(); i++){
            Student current = mCache.get(i);
            if(current.getId().equals(id)){
                return i;
            }
        }
        return -1;
    }
}
