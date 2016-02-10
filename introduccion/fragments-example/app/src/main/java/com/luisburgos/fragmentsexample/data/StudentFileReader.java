package com.luisburgos.fragmentsexample.data;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by luisburgos on 8/02/16.
 */
public class StudentFileReader {

    public static ArrayList<Student> readFileFromInternalStorage(Context context) {
        String eol = System.getProperty("line.separator");
        ArrayList<Student> arrayList = new ArrayList<Student>();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(
                    context.openFileInput(StudentDataSource.DEFAULT_FILE_NAME)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line + eol);
                arrayList.add(lineToStudent(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return arrayList;
        }
    }

    private static Student lineToStudent(String line) {
        String[] content = line.split(",");
        return new Student(content[0], content[1], content[2],content[3], content[4], content[5]);
    }

}
